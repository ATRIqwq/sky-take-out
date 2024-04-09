package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish ;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

/**
* @author 86136
* @description 针对表【dish(菜品)】的数据库操作Service
* @createDate 2024-04-03 12:27:18
*/
public interface DishService extends IService<Dish> {

    void save(DishDTO dishDTO);

    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    void deleteBatch(List<Long> ids);

    /**
     * 更新ID查询菜品，用于回显
     * @return
     */
    DishVO getDishWithFavor(Long id);

    /**
     * 修改菜品
     * @param dishDTO
     * @return
     */
    void updateDishWithFavor(DishDTO dishDTO);

    /**
     * 根据分类ID，获取菜品信息
     * @param categoryId
     * @return
     */
    List<Dish> listById(Long categoryId);

    /**
     * 停用或启用菜品
     * @param status
     * @param id
     * @return
     */
    void updateStatus(Integer status, Long id);


    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    List<DishVO> listWithFlavor(Dish dish);
}
