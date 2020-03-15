package com.changgou.feign;

import com.changgou.entity.Result;
import com.changgou.pojo.Sku;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


/**
 * 远程调用  商品微服务
 */
@FeignClient(name = "goods")
@RequestMapping("/sku")
public interface SkuFeign {

    @GetMapping
    public Result findAll();



    /**
     *
     * 通过外键查询库存集合
     * @param id
     * @return
     */
    @GetMapping("findSkuListBySpuId")
    public List<Sku> findSkuListBySpuId(@RequestParam(name = "id") String id);
}
