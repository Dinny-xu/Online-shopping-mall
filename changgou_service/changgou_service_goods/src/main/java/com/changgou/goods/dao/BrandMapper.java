package com.changgou.goods.dao;

import com.changgou.pojo.Brand;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;




public interface BrandMapper extends Mapper<Brand> {

    @Select("SELECT  t.* FROM tb_brand t    " + "INNER JOIN tb_category_brand l on t.id= l.brand_id " +
            "INNER JOIN tb_category c on l.category_id =c.id " +
            "where c.name = #{name}")
     List<Brand> findBrandListByCategoryName(@Param("name") String categoryName);
}
