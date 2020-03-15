package com.changgou.seckill.service;

import com.changgou.pojo.SeckillGoods;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

public interface SeckillGoodsService {

    /***
     * 查询所有
     * @return
     */
    List<SeckillGoods> findAll();

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    SeckillGoods findById(Long id);

    /***
     * 新增
     * @param seckillGoods
     */
    void add(SeckillGoods seckillGoods);

    /***
     * 修改
     * @param seckillGoods
     */
    void update(SeckillGoods seckillGoods);

    /***
     * 删除
     * @param id
     */
    void delete(Long id);

    /***
     * 多条件搜索
     * @param searchMap
     * @return
     */
    List<SeckillGoods> findList(Map<String, Object> searchMap);

    /***
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    Page<SeckillGoods> findPage(int page, int size);

    /***
     * 多条件分页查询
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    Page<SeckillGoods> findPage(Map<String, Object> searchMap, int page, int size);




}
