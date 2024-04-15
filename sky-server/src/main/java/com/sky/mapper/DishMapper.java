package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.Page;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish ;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
* @author 86136
* @description 针对表【dish(菜品)】的数据库操作Mapper
* @createDate 2024-04-03 12:27:18
* @Entity generator.domain.Dish
*/
@Mapper
public interface DishMapper extends BaseMapper<Dish> {

    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 根据条件统计菜品数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);
}




