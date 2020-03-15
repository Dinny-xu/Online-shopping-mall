package com.changgou.seckill.service;

import com.changgou.pojo.SeckillTime;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

public interface SeckillTimeService {

    /***
     * 查询所有
     * @return
     */
    List<SeckillTime> findAll();

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    SeckillTime findById(Integer id);

    /***
     * 新增
     * @param seckillTime
     */
    void add(SeckillTime seckillTime);

    /***
     * 修改
     * @param seckillTime
     */
    void update(SeckillTime seckillTime);

    /***
     * 删除
     * @param id
     */
    void delete(Integer id);

    /***
     * 多条件搜索
     * @param searchMap
     * @return
     */
    List<SeckillTime> findList(Map<String, Object> searchMap);

    /***
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    Page<SeckillTime> findPage(int page, int size);

    /***
     * 多条件分页查询
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    Page<SeckillTime> findPage(Map<String, Object> searchMap, int page, int size);




}
