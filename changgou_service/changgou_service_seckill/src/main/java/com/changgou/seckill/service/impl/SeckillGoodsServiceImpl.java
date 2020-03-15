package com.changgou.seckill.service.impl;

import com.changgou.seckill.dao.SeckillGoodsMapper;
import com.changgou.seckill.service.SeckillGoodsService;
import com.changgou.pojo.SeckillGoods;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

@Service
public class SeckillGoodsServiceImpl implements SeckillGoodsService {

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    /**
     * 查询全部列表
     * @return
     */
    @Override
    public List<SeckillGoods> findAll() {
        return seckillGoodsMapper.selectAll();
    }

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    @Override
    public SeckillGoods findById(Long id){
        return  seckillGoodsMapper.selectByPrimaryKey(id);
    }


    /**
     * 增加
     * @param seckillGoods
     */
    @Override
    public void add(SeckillGoods seckillGoods){
        seckillGoodsMapper.insert(seckillGoods);
    }


    /**
     * 修改
     * @param seckillGoods
     */
    @Override
    public void update(SeckillGoods seckillGoods){
        seckillGoodsMapper.updateByPrimaryKey(seckillGoods);
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void delete(Long id){
        seckillGoodsMapper.deleteByPrimaryKey(id);
    }


    /**
     * 条件查询
     * @param searchMap
     * @return
     */
    @Override
    public List<SeckillGoods> findList(Map<String, Object> searchMap){
        Example example = createExample(searchMap);
        return seckillGoodsMapper.selectByExample(example);
    }

    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<SeckillGoods> findPage(int page, int size){
        PageHelper.startPage(page,size);
        return (Page<SeckillGoods>)seckillGoodsMapper.selectAll();
    }

    /**
     * 条件+分页查询
     * @param searchMap 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public Page<SeckillGoods> findPage(Map<String,Object> searchMap, int page, int size){
        PageHelper.startPage(page,size);
        Example example = createExample(searchMap);
        return (Page<SeckillGoods>)seckillGoodsMapper.selectByExample(example);
    }

    /**
     * 构建查询对象
     * @param searchMap
     * @return
     */
    private Example createExample(Map<String, Object> searchMap){
        Example example=new Example(SeckillGoods.class);
        Example.Criteria criteria = example.createCriteria();
        if(searchMap!=null){
            // skuId
            if(searchMap.get("sku_id")!=null && !"".equals(searchMap.get("sku_id"))){
                criteria.andLike("sku_id","%"+searchMap.get("sku_id")+"%");
           	}
            // sku商品名称
            if(searchMap.get("sku_name")!=null && !"".equals(searchMap.get("sku_name"))){
                criteria.andLike("sku_name","%"+searchMap.get("sku_name")+"%");
           	}
            // sn
            if(searchMap.get("sku_sn")!=null && !"".equals(searchMap.get("sku_sn"))){
                criteria.andLike("sku_sn","%"+searchMap.get("sku_sn")+"%");
           	}
            // 秒杀商品图片
            if(searchMap.get("sku_image")!=null && !"".equals(searchMap.get("sku_image"))){
                criteria.andLike("sku_image","%"+searchMap.get("sku_image")+"%");
           	}

            // 秒杀价格
            if(searchMap.get("seckillPrice")!=null ){
                criteria.andEqualTo("seckillPrice",searchMap.get("seckillPrice"));
            }
            // 秒杀数量
            if(searchMap.get("seckillNum")!=null ){
                criteria.andEqualTo("seckillNum",searchMap.get("seckillNum"));
            }
            // 剩余数量
            if(searchMap.get("seckillSurplus")!=null ){
                criteria.andEqualTo("seckillSurplus",searchMap.get("seckillSurplus"));
            }
            // 限购数量
            if(searchMap.get("seckillLimit")!=null ){
                criteria.andEqualTo("seckillLimit",searchMap.get("seckillLimit"));
            }
            // 秒杀时间段id
            if(searchMap.get("timeId")!=null ){
                criteria.andEqualTo("timeId",searchMap.get("timeId"));
            }
            // 原价格
            if(searchMap.get("skuPrice")!=null ){
                criteria.andEqualTo("skuPrice",searchMap.get("skuPrice"));
            }
            // 排序
            if(searchMap.get("seq")!=null ){
                criteria.andEqualTo("seq",searchMap.get("seq"));
            }

        }
        return example;
    }

}
