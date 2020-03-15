package com.changgou.goods.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.Vo.Goods;
import com.changgou.goods.dao.*;
import com.changgou.goods.service.SpuService;
import com.changgou.pojo.Category;
import com.changgou.pojo.Sku;
import com.changgou.pojo.Spu;
import com.changgou.util.IdWorker;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import javafx.animation.Animation;
import jdk.net.SocketFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

@Service
public class SpuServiceImpl implements SpuService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private CategoryBrandMapper categoryBrandMapper;


    /**
     * 查询全部列表
     *
     * @return
     */
    @Override
    public List<Spu> findAll() {
        return spuMapper.selectAll();
    }

    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    @Override
    public Spu findById(String id) {
        return spuMapper.selectByPrimaryKey(id);
    }


    /**
     * 增加
     *
     * @param spu
     */
    @Override
    public void add(Spu spu) {

        long id = idWorker.nextId();
        spu.setId(String.valueOf(id));

        // int 10亿 +         Integer
        // bigint 10亿亿       Long

        //保存商品表 一条数据   分布式ID

        //库存表  多条数据  分布式ID


        spuMapper.insert(spu);
    }

    /**
     * 修改
     *
     * @param spu
     */
    @Override
    public void update(Spu spu) {
        spuMapper.updateByPrimaryKey(spu);
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public void delete(String id) {
        spuMapper.deleteByPrimaryKey(id);
    }


    /**
     * 条件查询
     *
     * @param searchMap
     * @return
     */
    @Override
    public List<Spu> findList(Map<String, Object> searchMap) {
        Example example = createExample(searchMap);
        return spuMapper.selectByExample(example);
    }

    /**
     * 分页查询
     *
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<Spu> findPage(int page, int size) {
        PageHelper.startPage(page, size);
        return (Page<Spu>) spuMapper.selectAll();
    }

    /**
     * 条件+分页查询
     *
     * @param searchMap 查询条件
     * @param page      页码
     * @param size      页大小
     * @return 分页结果
     */
    @Override
    public Page<Spu> findPage(Map<String, Object> searchMap, int page, int size) {
        PageHelper.startPage(page, size);
        Example example = createExample(searchMap);
        return (Page<Spu>) spuMapper.selectByExample(example);
    }


    /**
     * 根据ID 查询商品
     * @param id
     * @return
     */
    @Override
    public Goods findGoodsById(String id) {
        //查询spu
        Spu spu = spuMapper.selectByPrimaryKey(id);

        //查询sku 列表
        Example example = new Example(Sku.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("spuId", id);
        List<Sku> skuList = skuMapper.selectByExample(example);

        //封装，返回
        Goods goods = new Goods();
        goods.setSpu(spu);
        goods.setSkuList(skuList);
        return goods;
    }


    /**
     * 审核
     * @param id
     */
    @Override
    public void audit(String id,String status) {

        Spu spu = spuMapper.selectByPrimaryKey(id);
        if ("1".equalsIgnoreCase(spu.getIsDelete())) {

            throw new RuntimeException("删除的商品不能审核");
        }

        Spu spu1 = new Spu();
        spu1.setId(id);
        spu1.setStatus(status);

        spuMapper.updateByPrimaryKeySelective(spu1);

    }


    /**
     * 上下架
     * @param id
     * @param isMarketable
     */
    @Override
    public void isMarketable(String id, String isMarketable) {

        /**
         * 下架： 1：判断是否删除
         * 上架： 1：删除  2：审核
         */
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if ("1".equalsIgnoreCase(spu.getIsDelete())) {
            //删除  不审核
            throw new RuntimeException("删除的商品不能上下架");
        }

        Spu spu1 = new Spu();
        spu1.setId(id);
        spu1.setIsMarketable(isMarketable);

        if ("0".equalsIgnoreCase(isMarketable)) {
            //下架

            spuMapper.updateByPrimaryKeySelective(spu);
        }else {
            if ("1".equals(spu.getStatus())) {
                spuMapper.updateByPrimaryKeySelective(spu);
            }else {
                throw new RuntimeException("审核不通过的商品不能上架");
            }
        }
    }


    /**
     * 批量上下架
     * @param ids
     * @return
     */
    @Override
    public int PutMany(String[] ids) {
        Spu spu = new Spu();
        spu.setIsMarketable("1");

        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", Arrays.asList(ids));
        criteria.andEqualTo("isMarketable","0");
        criteria.andEqualTo("status","1");
        criteria.andEqualTo("isDelete","0");
        return spuMapper.updateByExampleSelective(spu, example);

    }


    /**
     * 删除   还原
     * @param id
     * @param isDelete
     */
    @Override
    public void isDelete(String id, String isDelete) {

        Spu spu = new Spu();
        spu.setId(id);
        spu.setIsDelete(isDelete);
        spu.setIsMarketable("0");
        spuMapper.updateByPrimaryKeySelective(spu);

    }

    /**
     * 添加商品库存
     *
     * @param goods
     */
    @Override
    public void save(Goods goods) {

        //给商品创建唯一主键，分布式ID 生成
        long id = idWorker.nextId();

        //商品表
        Spu spu = goods.getSpu();
        spu.setId(String.valueOf(id));

        //设置成下架商品，解决硬编码问题
        spu.setIsMarketable(Spu.NO_ISMARKETABLE);

        //商品不允许删除
        spu.setIsDelete("0");
        spu.setStatus("0");
        spuMapper.insertSelective(spu);
        //库存表，集合
        List<Sku> skuList = goods.getSkuList();
        for (Sku sku : skuList) {

            sku.setId(String.valueOf(idWorker.nextId()));

            //sku 名称
            String spec = sku.getSpec();

            Map<String, String> map = JSON.parseObject(spec, Map.class);

            Set<Map.Entry<String, String>> entries = map.entrySet();

            //标准产品单位的名称
            String title = spu.getName();

            for (Map.Entry<String, String> entry : entries) {
                title += " " + entry.getValue();
            }
            System.out.println(title);

            sku.setName(title);

            //时间
            sku.setCreateTime(new Date());
            sku.setUpdateTime(new Date());

            sku.setSpuId(String.valueOf(id));
            sku.setCategoryId(spu.getCategory3Id());

            //商品分类名称
            Category category = categoryMapper.selectByPrimaryKey(spu.getCategory3Id());
            sku.setCategoryName(category.getName());

            //品牌名称
            sku.setBrandName(brandMapper.selectByPrimaryKey(spu.getBrandId()).getName());
            skuMapper.insertSelective(sku);

        }
    }

    /**
     * 构建查询对象
     *
     * @param searchMap
     * @return
     */
    private Example createExample(Map<String, Object> searchMap) {
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        if (searchMap != null) {
            // 主键
            if (searchMap.get("id") != null && !"".equals(searchMap.get("id"))) {
                criteria.andLike("id", "%" + searchMap.get("id") + "%");
            }
            // 货号
            if (searchMap.get("sn") != null && !"".equals(searchMap.get("sn"))) {
                criteria.andLike("sn", "%" + searchMap.get("sn") + "%");
            }
            // SPU名
            if (searchMap.get("name") != null && !"".equals(searchMap.get("name"))) {
                criteria.andLike("name", "%" + searchMap.get("name") + "%");
            }
            // 副标题
            if (searchMap.get("caption") != null && !"".equals(searchMap.get("caption"))) {
                criteria.andLike("caption", "%" + searchMap.get("caption") + "%");
            }
            // 图片
            if (searchMap.get("image") != null && !"".equals(searchMap.get("image"))) {
                criteria.andLike("image", "%" + searchMap.get("image") + "%");
            }
            // 图片列表
            if (searchMap.get("images") != null && !"".equals(searchMap.get("images"))) {
                criteria.andLike("images", "%" + searchMap.get("images") + "%");
            }
            // 售后服务
            if (searchMap.get("sale_service") != null && !"".equals(searchMap.get("sale_service"))) {
                criteria.andLike("sale_service", "%" + searchMap.get("sale_service") + "%");
            }
            // 介绍
            if (searchMap.get("introduction") != null && !"".equals(searchMap.get("introduction"))) {
                criteria.andLike("introduction", "%" + searchMap.get("introduction") + "%");
            }
            // 规格列表
            if (searchMap.get("spec_items") != null && !"".equals(searchMap.get("spec_items"))) {
                criteria.andLike("spec_items", "%" + searchMap.get("spec_items") + "%");
            }
            // 参数列表
            if (searchMap.get("para_items") != null && !"".equals(searchMap.get("para_items"))) {
                criteria.andLike("para_items", "%" + searchMap.get("para_items") + "%");
            }
            // 是否上架
            if (searchMap.get("is_marketable") != null && !"".equals(searchMap.get("is_marketable"))) {
                criteria.andLike("is_marketable", "%" + searchMap.get("is_marketable") + "%");
            }
            // 是否启用规格
            if (searchMap.get("is_enable_spec") != null && !"".equals(searchMap.get("is_enable_spec"))) {
                criteria.andLike("is_enable_spec", "%" + searchMap.get("is_enable_spec") + "%");
            }
            // 是否删除
            if (searchMap.get("is_delete") != null && !"".equals(searchMap.get("is_delete"))) {
                criteria.andLike("is_delete", "%" + searchMap.get("is_delete") + "%");
            }
            // 审核状态
            if (searchMap.get("status") != null && !"".equals(searchMap.get("status"))) {
                criteria.andLike("status", "%" + searchMap.get("status") + "%");
            }

            // 品牌ID
            if (searchMap.get("brandId") != null) {
                criteria.andEqualTo("brandId", searchMap.get("brandId"));
            }
            // 一级分类
            if (searchMap.get("category1Id") != null) {
                criteria.andEqualTo("category1Id", searchMap.get("category1Id"));
            }
            // 二级分类
            if (searchMap.get("category2Id") != null) {
                criteria.andEqualTo("category2Id", searchMap.get("category2Id"));
            }
            // 三级分类
            if (searchMap.get("category3Id") != null) {
                criteria.andEqualTo("category3Id", searchMap.get("category3Id"));
            }
            // 模板ID
            if (searchMap.get("templateId") != null) {
                criteria.andEqualTo("templateId", searchMap.get("templateId"));
            }
            // 运费模板id
            if (searchMap.get("freightId") != null) {
                criteria.andEqualTo("freightId", searchMap.get("freightId"));
            }
            // 销量
            if (searchMap.get("saleNum") != null) {
                criteria.andEqualTo("saleNum", searchMap.get("saleNum"));
            }
            // 评论数
            if (searchMap.get("commentNum") != null) {
                criteria.andEqualTo("commentNum", searchMap.get("commentNum"));
            }

        }
        return example;
    }


}
