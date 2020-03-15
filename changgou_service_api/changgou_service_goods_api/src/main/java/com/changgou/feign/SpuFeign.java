package com.changgou.feign;



import com.changgou.pojo.Spu;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * 商品远程调用
 */

@FeignClient(name = "goods")
@RequestMapping("/spu")
public interface SpuFeign {


    @GetMapping("findSpuById/{id}")
    public Spu findSpuById(@PathVariable(name = "id") String id);
}
