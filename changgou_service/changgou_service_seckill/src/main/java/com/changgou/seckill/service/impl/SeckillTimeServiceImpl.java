package com.changgou.seckill.service.impl;

import com.changgou.seckill.dao.SeckillTimeMapper;
import com.changgou.seckill.service.SeckillTimeService;
import com.changgou.pojo.SeckillTime;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

@Service
public class SeckillTimeServiceImpl implements SeckillTimeService {

    @Autowired
    private SeckillTimeMapper seckillTimeMapper;

    /**
     * 查询全部列表
     * @return
     */
    @Override
    public List<SeckillTime> findAll() {
        return seckillTimeMapper.selectAll();
    }

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    @Override
    public SeckillTime findById(Integer id){
        return  seckillTimeMapper.selectByPrimaryKey(id);
    }


    /**
     * 增加
     * @param seckillTime
     */
    @Override
    public void add(SeckillTime seckillTime){
        seckillTimeMapper.insert(seckillTime);
    }


    /**
     * 修改
     * @param seckillTime
     */
    @Override
    public void update(SeckillTime seckillTime){
        seckillTimeMapper.updateByPrimaryKey(seckillTime);
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void delete(Integer id){
        seckillTimeMapper.deleteByPrimaryKey(id);
    }


    /**
     * 条件查询
     * @param searchMap
     * @return
     */
    @Override
    public List<SeckillTime> findList(Map<String, Object> searchMap){
        Example example = createExample(searchMap);
        return seckillTimeMapper.selectByExample(example);
    }

    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<SeckillTime> findPage(int page, int size){
        PageHelper.startPage(page,size);
        return (Page<SeckillTime>)seckillTimeMapper.selectAll();
    }

    /**
     * 条件+分页查询
     * @param searchMap 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public Page<SeckillTime> findPage(Map<String,Object> searchMap, int page, int size){
        PageHelper.startPage(page,size);
        Example example = createExample(searchMap);
        return (Page<SeckillTime>)seckillTimeMapper.selectByExample(example);
    }

    /**
     * 构建查询对象
     * @param searchMap
     * @return
     */
    private Example createExample(Map<String, Object> searchMap){
        Example example=new Example(SeckillTime.class);
        Example.Criteria criteria = example.createCriteria();
        if(searchMap!=null){
            // 时间段名称
            if(searchMap.get("name")!=null && !"".equals(searchMap.get("name"))){
                criteria.andLike("name","%"+searchMap.get("name")+"%");
           	}
            // 开始时间
            if(searchMap.get("start_time")!=null && !"".equals(searchMap.get("start_time"))){
                criteria.andLike("start_time","%"+searchMap.get("start_time")+"%");
           	}
            // 截至时间
            if(searchMap.get("end_time")!=null && !"".equals(searchMap.get("end_time"))){
                criteria.andLike("end_time","%"+searchMap.get("end_time")+"%");
           	}
            // 状态
            if(searchMap.get("status")!=null && !"".equals(searchMap.get("status"))){
                criteria.andLike("status","%"+searchMap.get("status")+"%");
           	}

            // id
            if(searchMap.get("id")!=null ){
                criteria.andEqualTo("id",searchMap.get("id"));
            }

        }
        return example;
    }

}
