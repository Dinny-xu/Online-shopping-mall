package com.changgou.Item.controller;


import com.changgou.entity.Page;
import com.changgou.search.feign.SearchFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/wsearch")
public class SearchController {


    //注入远程调用
    @Autowired
    private SearchFeign searchFeign;


    /**
     * 问题总结：
     * 4G+64GB
     * 特殊符号+
     * 1- 远程调用  Feign  传递入参 + 变成空串
     * 2- 响应时 + 变成了 %2B
     * <p>
     * 页面传递到服务器时  入参 + 是没有问题
     *
     * @param searchMap
     * @param
     * @return
     */

    public void handlerParam(Map<String, String> searchMap) {
        //本次  处理+

        if (!StringUtils.isEmpty(searchMap)) {
            Set<Map.Entry<String, String>> entries = searchMap.entrySet();
            for (Map.Entry<String, String> entry : entries) {

                if (entry.getValue().contains("+")) {
                    searchMap.put(entry.getKey(), entry.getValue().replace("+", "%2B"));
                }
            }
        }
    }



    //搜索 根据关键词
    @GetMapping("/list")
    public String list(@RequestParam Map<String,String> searchMap, Model model) {

        //特殊符号提前处理
        handlerParam(searchMap);


        //根据关键词查询
        Map resultMap = searchFeign.search(searchMap);
        model.addAttribute("resultMap", resultMap);

        //创建分页对象
        //当前页
        //每页数
        //总条数
        Page<Object> page = new Page( Long.parseLong(String.valueOf(resultMap.get("totalElements"))),
                Integer.parseInt(String.valueOf(resultMap.get("pageNum"))), Page.pageSize);

        model.addAttribute("page", page);

        //过滤条件拼接
        StringBuilder sb = new StringBuilder();
        sb.append("/wsearch/list").append("?pvid=f");
        if (!StringUtils.isEmpty(searchMap)) {

            Set<Map.Entry<String, String>> entries = searchMap.entrySet();
            for (Map.Entry<String, String> entry : entries) {

                if(!"sortField".equals(entry.getKey())  &&  !"sortRule".equals(entry.getKey())) {
                    sb.append("&").append(entry.getKey()).append("=").append(entry.getValue());
                }
            }
        }
        model.addAttribute("url", sb.toString());

        //将关键词进行回显
        model.addAttribute("searchMap", searchMap);

        //跳转搜索页面视图
        return "search";
    }
}
