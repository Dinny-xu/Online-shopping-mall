package com.changgou.seckill.controller;
import com.changgou.entity.PageResult;
import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.seckill.service.SeckillActivityService;
import com.changgou.pojo.SeckillActivity;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
@RestController
@CrossOrigin
@RequestMapping("/seckillActivity")
public class SeckillActivityController {


    @Autowired
    private SeckillActivityService seckillActivityService;

    /**
     * 查询全部数据
     * @return
     */
    @GetMapping
    public Result findAll(){
        List<SeckillActivity> seckillActivityList = seckillActivityService.findAll();
        return new Result(true, StatusCode.OK,"查询成功",seckillActivityList) ;
    }

    /***
     * 根据ID查询数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result findById(@PathVariable Long id){
        SeckillActivity seckillActivity = seckillActivityService.findById(id);
        return new Result(true,StatusCode.OK,"查询成功",seckillActivity);
    }


    /***
     * 新增数据
     * @param seckillActivity
     * @return
     */
    @PostMapping
    public Result add(@RequestBody SeckillActivity seckillActivity){
        seckillActivityService.add(seckillActivity);
        return new Result(true,StatusCode.OK,"添加成功");
    }


    /***
     * 修改数据
     * @param seckillActivity
     * @param id
     * @return
     */
    @PutMapping(value="/{id}")
    public Result update(@RequestBody SeckillActivity seckillActivity,@PathVariable Long id){
        seckillActivity.setId(id);
        seckillActivityService.update(seckillActivity);
        return new Result(true,StatusCode.OK,"修改成功");
    }


    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}" )
    public Result delete(@PathVariable Long id){
        seckillActivityService.delete(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    /***
     * 多条件搜索品牌数据
     * @param searchMap
     * @return
     */
    @GetMapping(value = "/search" )
    public Result findList(@RequestParam Map searchMap){
        List<SeckillActivity> list = seckillActivityService.findList(searchMap);
        return new Result(true,StatusCode.OK,"查询成功",list);
    }


    /***
     * 分页搜索实现
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    @GetMapping(value = "/search/{page}/{size}" )
    public Result findPage(@RequestParam Map searchMap, @PathVariable  int page, @PathVariable  int size){
        Page<SeckillActivity> pageList = seckillActivityService.findPage(searchMap, page, size);
        PageResult pageResult=new PageResult(pageList.getTotal(),pageList.getResult());
        return new Result(true,StatusCode.OK,"查询成功",pageResult);
    }


}
