package com.changgou.Item.controller;

import com.changgou.Item.service.StaticPageService;
import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：炎炎
 * @date ：Created in 2019/10/13 11:15
 * @description：执行静态初始化页面
 */


@RestController
@RequestMapping("/item")
public class ItemController {


    @Autowired
    private StaticPageService staticPageService;


    @GetMapping("/index")
    public Result index(String id) {

        staticPageService.index(id);

        return new Result(true, StatusCode.OK, "生成静态化页面成功");
    }

}
