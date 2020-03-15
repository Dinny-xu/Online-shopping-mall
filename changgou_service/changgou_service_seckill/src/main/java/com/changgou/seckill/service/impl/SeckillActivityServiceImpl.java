package com.changgou.seckill.service.impl;

import com.changgou.seckill.dao.SeckillActivityMapper;
import com.changgou.seckill.service.SeckillActivityService;
import com.changgou.pojo.SeckillActivity;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

@Service
public class SeckillActivityServiceImpl implements SeckillActivityService {

    @Autowired
    private SeckillActivityMapper seckillActivityMapper;

    /**
     * 查询全部列表
     * @return
     */
    @Override
    public List<SeckillActivity> findAll() {
        return seckillActivityMapper.selectAll();
    }

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    @Override
    public SeckillActivity findById(Long id){
        return  seckillActivityMapper.selectByPrimaryKey(id);
    }


    /**
     * 增加
     * @param seckillActivity
     */
    @Override
    public void add(SeckillActivity seckillActivity){
        seckillActivityMapper.insert(seckillActivity);
    }


    /**
     * 修改
     * @param seckillActivity
     */
    @Override
    public void update(SeckillActivity seckillActivity){
        seckillActivityMapper.updateByPrimaryKey(seckillActivity);
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void delete(Long id){
        seckillActivityMapper.deleteByPrimaryKey(id);
    }


    /**
     * 条件查询
     * @param searchMap
     * @return
     */
    @Override
    public List<SeckillActivity> findList(Map<String, Object> searchMap){
        Example example = createExample(searchMap);
        return seckillActivityMapper.selectByExample(example);
    }

    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<SeckillActivity> findPage(int page, int size){
        PageHelper.startPage(page,size);
        return (Page<SeckillActivity>)seckillActivityMapper.selectAll();
    }

    /**
     * 条件+分页查询
     * @param searchMap 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public Page<SeckillActivity> findPage(Map<String,Object> searchMap, int page, int size){
        PageHelper.startPage(page,size);
        Example example = createExample(searchMap);
        return (Page<SeckillActivity>)seckillActivityMapper.selectByExample(example);
    }

    /**
     * 构建查询对象
     * @param searchMap
     * @return
     */
    private Example createExample(Map<String, Object> searchMap){
        Example example=new Example(SeckillActivity.class);
        Example.Criteria criteria = example.createCriteria();
        if(searchMap!=null){
            // 秒杀活动标题
            if(searchMap.get("title")!=null && !"".equals(searchMap.get("title"))){
                criteria.andLike("title","%"+searchMap.get("title")+"%");
           	}
            // 状态
            if(searchMap.get("status")!=null && !"".equals(searchMap.get("status"))){
                criteria.andLike("status","%"+searchMap.get("status")+"%");
           	}


        }
        return example;
    }

}
