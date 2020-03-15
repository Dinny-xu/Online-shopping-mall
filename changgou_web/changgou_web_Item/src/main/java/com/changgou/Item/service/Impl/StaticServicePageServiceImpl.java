package com.changgou.Item.service.Impl;

import com.alibaba.fastjson.JSON;
import com.changgou.Item.service.StaticPageService;
import com.changgou.feign.CategoryFeign;
import com.changgou.feign.SkuFeign;
import com.changgou.feign.SpuFeign;
import com.changgou.pojo.Spu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;


/**
 * @author ：炎炎
 * @date ：Created in 2019/10/13 11:14
 * @description：定时、人为触发 静态初始化页面
 */

@Service
public class StaticServicePageServiceImpl implements StaticPageService {


    @Autowired
    private TemplateEngine templateEngine;


    @Autowired
    private CategoryFeign categoryFeign;

    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private SpuFeign spuFeign;

    @Value("${pagepath}")
    private String pagepath;

    @Override
    public void index(String id) {

        Map map = new HashMap<>();

        //4：商品数据
        Spu spu = spuFeign.findSpuById(id);
        map.put("spu", spu);

        //1:商品分类  1 2 3
        map.put("category1", categoryFeign.findcategoryById(spu.getCategory1Id()));
        map.put("category2", categoryFeign.findcategoryById(spu.getCategory2Id()));
        map.put("category3", categoryFeign.findcategoryById(spu.getCategory3Id()));

        //2：商品图片集合
        String[] split = spu.getImages().split(",");
        map.put("imageList", split);

        //3：库存数据（多个） 集合
        map.put("skuList", skuFeign.findSkuListBySpuId(id));

        //5：规格（规格选项集合） 集合 Map<String,Set<String>>
        //{"颜色":["黑色","金色"],"尺码":["250度","200度","100度","150度","300度"]}
        String specItems = spu.getSpecItems();
        map.put("specificationList", JSON.parseObject(specItems, Map.class));


        //数据
        Context context = new Context();
        context.setVariables(map);

        //输出
        Writer writer = null;

        try {
            writer = new PrintWriter(pagepath + "/" + id + ".html", "utf-8");
            //执行静态化处理
            templateEngine.process("item", context, writer);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
