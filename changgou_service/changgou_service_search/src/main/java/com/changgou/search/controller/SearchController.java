package com.changgou.search.controller;


import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/search")
public class SearchController {


    @Autowired
    private SearchService searchService;

    //开始搜索ES 数据库
    @GetMapping
    public Map search(@RequestParam Map<String, String> searchMap) {

        return searchService.search(searchMap);
    }


    /**
     * 导入数据
     * @return
     */
    @GetMapping("/importDate")
    public Result importDate() {

        searchService.importDate();
        return new Result(true, StatusCode.OK, "数据导入成功");
    }
}
