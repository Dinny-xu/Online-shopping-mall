package com.changgou.goods.controller;

import com.changgou.Vo.Goods;
import com.changgou.entity.PageResult;
import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.goods.service.SpuService;
import com.changgou.pojo.Spu;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/spu")
public class SpuController {


    @Autowired
    private SpuService spuService;

    /**
     * 查询全部数据
     *
     * @return
     */
    @GetMapping
    public Result findAll() {
        List<Spu> spuList = spuService.findAll();
        return new Result(true, StatusCode.OK, "查询成功", spuList);
    }


    /**
     *
     * 根据商品ID 查数据
     * @param id
     * @return
     */
    @GetMapping("findSpuById/{id}")
    public Spu findSpuById(@PathVariable String id) {

        return spuService.findById(id);
    }



    /***
     * 根据ID查询数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result findById(@PathVariable String id) {
        Goods goods = spuService.findGoodsById(id);
        return new Result(true, StatusCode.OK, "查询成功", goods);
    }


    /***
     * 新增数据
     * @param goods
     * @return
     */
    @PostMapping
    public Result add(@RequestBody Goods goods) {
        spuService.save(goods);
        return new Result(true, StatusCode.OK, "添加成功");
    }


    /***
     * 修改数据
     * @param spu
     * @param id
     * @return
     */
    @PutMapping(value = "/{id}")
    public Result update(@RequestBody Spu spu, @PathVariable String id) {
        spu.setId(id);
        spuService.update(spu);
        return new Result(true, StatusCode.OK, "修改成功");
    }


    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable String id) {
        spuService.delete(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /***
     * 多条件搜索品牌数据
     * @param searchMap
     * @return
     */
    @GetMapping(value = "/search")
    public Result findList(@RequestParam Map searchMap) {
        List<Spu> list = spuService.findList(searchMap);
        return new Result(true, StatusCode.OK, "查询成功", list);
    }


    /***
     * 分页搜索实现
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    @GetMapping(value = "/search/{page}/{size}")
    public Result findPage(@RequestParam Map searchMap, @PathVariable int page, @PathVariable int size) {
        Page<Spu> pageList = spuService.findPage(searchMap, page, size);
        PageResult pageResult = new PageResult(pageList.getTotal(), pageList.getResult());
        return new Result(true, StatusCode.OK, "查询成功", pageResult);
    }


    /**
     * 审核
     * @param id
     * @return
     */
    @PutMapping("/audit")
    public Result audit(String id, String status) {
        spuService.audit(id,status);
        return new Result();
    }


    /**
     * 上下架
     * @param id
     * @param isMarketable
     * @return
     */
    @PutMapping("/isMarketable")
    public Result isMarketable(String id,String isMarketable) {
        spuService.isMarketable(id, isMarketable);

        return new Result();
    }


    /**
     *  删除  还原
     * @param id
     * @param isDelete
     * @return
     */
    @PutMapping("/isDelete")
    public Result isDelete(String id,String isDelete) {
        spuService.isDelete(id, isDelete);

        return new Result();
    }


    /**
     * 批量上下架
     * @param ids
     * @return
     */
    @PutMapping("PutMany")
    public Result PutMany(@RequestParam String[] ids) {
        int count = spuService.PutMany(ids);
        return new Result(true, StatusCode.OK, "上架" + count + "个商品");
    }

}
