package com.changgou.search.service.Impl;

import com.alibaba.fastjson.JSON;
import com.changgou.entity.Page;
import com.changgou.entity.Result;
import com.changgou.feign.SkuFeign;
import com.changgou.pojo.Sku;
import com.changgou.search.dao.SearchMapper;
import com.changgou.search.pojo.SkuInfo;
import com.changgou.search.service.SearchService;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {


    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private SearchMapper searchMapper;


    //Dao Mapper 查询Mysql
    //ES索引库   复杂查询
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;


    /**
     * SpringDataElasticSearch
     * <p>
     * 1)ElasticsearchTemplate  主要是负责查询  性能非常好
     * 2)添加 修改 删除  ElasticsearchRepository  接口  通用Mapper
     *
     * @param searchMap
     * @return
     */
    @Override
    public Map search(Map<String, String> searchMap) {

        //条件对象  构建对象  往里面放数据
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();

        //返回值  Map
        Map resultMap = new HashMap();

        //组合查询  （多条件查询）
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        //条件：关键词 keywords 手机
        builder.withQuery(QueryBuilders.matchQuery("name", searchMap.get("keywords")));

        //判断品牌是否有值
        if (!StringUtils.isEmpty(searchMap.get("brand"))) {
            //品牌  过滤条件
            boolQueryBuilder.filter(QueryBuilders.termQuery("brandName", searchMap.get("brand")));
        }


        //规格（不止一个）过滤条件
        //颜色   黑色  金色  蓝色  粉色
        //版本 6GB+12GB  6GB+64GB  4GB+64GB

        Set<Map.Entry<String, String>> entries = searchMap.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            if (entry.getKey().contains("spec_")) {
                //规格过滤条件查询
                boolQueryBuilder.filter(QueryBuilders.
                        termQuery("specMap." + entry.getKey().substring(5)
                                + ".keyword", entry.getValue().replace("%2B", "+")));
            }
        }

          /*      "spec": """{ "颜色"  : "高清舒适", "尺码": "100度"}""",

           "specMap": {
            "颜色": "高清舒适",
             "尺码": "100度"
        }
        */

        //价格区间  过滤条件
        if (!StringUtils.isEmpty(searchMap.get("price"))) {

            //切割
            String[] p = searchMap.get("price").split("-");
            if (p.length == 2) {
                //价格区间过滤条件
                boolQueryBuilder.filter(QueryBuilders
                        .rangeQuery("price").from(p[0], true)
                        .to(p[1], false));

            } else {
                boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(p[0]));
            }

        }

        //排序
        if (!StringUtils.isEmpty(searchMap.get("sortField"))) {

            if ("DESC".equals(searchMap.get("sortRule"))) {

                builder.withSort(SortBuilders.fieldSort(searchMap.get("sortField")).
                        order(SortOrder.DESC));
            } else {
                builder.withSort(SortBuilders.fieldSort(searchMap.get("sortField"))
                        .order(SortOrder.ASC));
            }

        }
        //分页
        //判断是否有当前页  如果有使用当前页  如果没有  默认是1页
        if (null == searchMap.get("pageNum")) {
            resultMap.put("pageNum", Page.pageNum);
            builder.withPageable(PageRequest.of(Page.pageNum - 1, Page.pageSize));
        } else {
            resultMap.put("pageNum", searchMap.get("pageNum"));
            builder.withPageable(PageRequest.of(Integer.parseInt(searchMap.get("pageNum")) - 1, Page.pageSize));
        }


        //1：查询 品牌集合  分组查询   AggregationBuilders 按照什么分组查询构建对象
        String skuBrand = "skuBrand";
        builder.addAggregation(AggregationBuilders.terms(skuBrand).field("brandName"));


        //2：规格集合（每一个规格还要查询规格选项集合）
        String skuSpec = "skuSpec";
        builder.addAggregation(AggregationBuilders.terms(skuSpec).field("spec.keywords"));


        builder.withFilter(boolQueryBuilder);

        //高亮
        HighlightBuilder.Field field = new HighlightBuilder.Field("name");
        field.preTags("<font style = 'color:red'>");
        field.postTags("</font>");
        builder.withHighlightFields(field);

        //执行查询
        AggregatedPage<SkuInfo> page = elasticsearchTemplate.queryForPage(builder.build(), SkuInfo.class
                , new SearchResultMapper() {
                    @Override
                    public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {

                        //结果集
                        List<SkuInfo> list = new ArrayList<>();

                        //分组 品牌  规格
                        Aggregations aggregations = searchResponse.getAggregations();
                        SearchHits hits = searchResponse.getHits();

                        SearchHit[] hits1 = hits.getHits();
                        for (SearchHit documentFields : hits1) {

                            String sourceAsString = documentFields.getSourceAsString();

                            SkuInfo skuInfo = JSON.parseObject(sourceAsString, SkuInfo.class);

                            if (null != documentFields.getHighlightFields() && documentFields.getHighlightFields().size() > 0) {
                                String s = documentFields.getHighlightFields().get("name").getFragments()[0].toString();

                               /* if (null != s) {
                                    skuInfo.setName(s);
                                }*/

                            }

                            list.add(skuInfo);

                        }
                        //   return new AggregatedPageImpl<T>(list,pageable,hits,totalHits,aggregations);
                        return null;
                    }
                });


        //获取出品牌集合 （分组后的）
        List<String> brandList = new ArrayList<>();
        Terms terms = (Terms) page.getAggregation(skuBrand);

        terms.getBuckets().stream().forEach(c -> brandList.add(c.getKeyAsString()));

        //获取出规格集合 （分组后的）
        Terms specTerms = (Terms) page.getAggregation(skuSpec);
        List<String> specList = specTerms.getBuckets()
                .stream().map(c -> c.getKeyAsString()).collect(Collectors.toList());


        /**
         * 处理前数据的样子
         *
         * "spec": {"颜色"："黑色","尺寸"："19英寸"}
         *
         * "spec": {"颜色"："高清舒适","尺寸"："100度"}
         *
         */

        Map<String, Set<String>> specMap = builderSpecData(specList);

        /**
         * 处理后数据的样子
         *
         * 颜色：黑  高清  。。  Map1  K  V SET
         *
         * 尺码： 19   100度   Map2
         *
         * 分辨    Map3
         *
         * 内存
         *
         */

        //3：结果集
        List<SkuInfo> content = page.getContent();

        //4:总条数  当前页  每页数 （创建分页对象）


        //1-总页数  2-结果集   3-分组查询结果
        long totalElements = page.getTotalElements();
        int totalPages = page.getTotalPages();
        resultMap.put("rows", content);
        resultMap.put("brandList", brandList);
        resultMap.put("totalElements", totalElements);
        resultMap.put("totalPages", totalPages);
        resultMap.put("specList", specMap);

        return resultMap;
    }


    /**
     * 处理前数据的样子
     * <p>
     * "spec": {"颜色"："黑色","尺寸"："19英寸"}
     * <p>
     * "spec": {"颜色"："高清舒适","尺寸"："100度"}
     */
    public Map<String, Set<String>> builderSpecData(List<String> specList) {

        HashMap<String, Set<String>> specMap = new HashMap<>();

        for (String spec : specList) {
            /*
            {"颜色"："黑色","尺寸"："19英寸"}
            {"颜色"："高清舒适","尺寸"："100度"}
            {"颜色"："黑色","尺寸"："19英寸"}
             */

            Map<String, String> map = JSON.parseObject(spec, Map.class);
            Set<Map.Entry<String, String>> entries = map.entrySet();
            for (Map.Entry<String, String> entry : entries) {

                Set<String> set = specMap.get(entry.getKey());
                if (null == set) {

                    set = new HashSet<>();
                }
                //追加属性
                set.add(entry.getValue());

                specMap.put(entry.getKey(), set);
            }


        }
        return specMap;


        /**
         * 处理后数据的样子
         *
         * 颜色：黑  高清  。。  Map1  K  V SET
         *
         * 尺码： 19   100度   Map2
         *
         * 分辨    Map3
         *
         * 内存
         *
         */

    }


    //开始导入数据
    @Override
    public void importDate() {

        //查询 商品微服务（远程调用） 查询所有库存数据集合
        Result result = skuFeign.findAll();
        Object data = result.getData(); //强转异常

        //上面的data转成json 格式字符串
        //将json格式字符串转成 SkuInfo

        List<SkuInfo> skuInfoList = JSON.parseArray(JSON.toJSONString(data), SkuInfo.class);

     /*   for (SkuInfo skuInfo : skuInfoList) {
            skuInfo.setSpecMap(JSON.parseObject(skuInfo.getSpec(), Map.class));
        }
        searchMapper.saveAll(skuInfoList);

    }*/

    }
}
