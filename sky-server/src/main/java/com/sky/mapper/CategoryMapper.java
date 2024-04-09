package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 86136
* @description 针对表【category(菜品及套餐分类)】的数据库操作Mapper
* @createDate 2024-03-26 20:04:08
* @Entity com.sky.entity.Category
*/

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

    Page<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);


}




