package com.changgou.goods.dao;

import com.changgou.pojo.Brand;
import com.changgou.pojo.Spec;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SpecMapper extends Mapper<Spec> {

    @Select("SELECT t.* FROM tb_spec t  " +
            "INNER JOIN tb_category tb on t.template_id = tb.template_id " +
            "WHERE tb.name =#{name}")
    List<Brand> findSpecListByCategoryName(@Param("name") String categoryName);
}
