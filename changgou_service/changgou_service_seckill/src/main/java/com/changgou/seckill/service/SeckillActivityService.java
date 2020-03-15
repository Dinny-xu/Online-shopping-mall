package com.changgou.seckill.service;

import com.changgou.pojo.SeckillActivity;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

public interface SeckillActivityService {

    /***
     * 查询所有
     * @return
     */
    List<SeckillActivity> findAll();

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    SeckillActivity findById(Long id);

    /***
     * 新增
     * @param seckillActivity
     */
    void add(SeckillActivity seckillActivity);

    /***
     * 修改
     * @param seckillActivity
     */
    void update(SeckillActivity seckillActivity);

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
    List<SeckillActivity> findList(Map<String, Object> searchMap);

    /***
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    Page<SeckillActivity> findPage(int page, int size);

    /***
     * 多条件分页查询
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    Page<SeckillActivity> findPage(Map<String, Object> searchMap, int page, int size);




}
