package com.changgou.search.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 *
 * 搜索 远程调用
 *
 */
@FeignClient(name = "search")
@RequestMapping("/search")
public interface SearchFeign {


    //开始搜索 ES
    @GetMapping
    public Map search(@RequestParam(name = "searchMap") Map<String, String> searchMap);


}
