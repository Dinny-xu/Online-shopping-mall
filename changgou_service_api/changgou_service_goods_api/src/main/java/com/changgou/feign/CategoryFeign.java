package com.changgou.feign;


import com.changgou.pojo.Category;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 *
 *
 */

@FeignClient(name = "goods")
@RequestMapping("/category")
public interface CategoryFeign {

    /**
     *
     * 根据 ID 查数据
     * @param id
     * @return
     */
    @GetMapping("findCategoryById/{id}")
    public Category findcategoryById(@PathVariable(name = "id") Integer id);
}
