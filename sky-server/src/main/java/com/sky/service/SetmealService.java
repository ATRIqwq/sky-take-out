package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal ;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 86136
* @description 针对表【setmeal(套餐)】的数据库操作Service
* @createDate 2024-04-03 19:58:48
*/
@Service
public interface SetmealService extends IService<Setmeal> {

    /**
     * 新增套餐
     * @param setmealDTO
     * @return
     */
    void saveWithDish(SetmealDTO setmealDTO);

    /**
     * 套餐分页查询
     * @return
     */
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 删除套餐
     * @param ids
     * @return
     */
    void deleteBatch(List<Long> ids);

    /**
     * 根据ID查询套餐数据，用于回显
     * @param id
     * @return
     */
    SetmealVO listWithDish(Long id);

    /**
     * 套餐的起售停售
     * @param status
     * @param id
     * @return
     */
    void startOrStop(Integer status, Long id);


    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    List<Setmeal> listSetmeal(Setmeal setmeal);

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    List<DishItemVO> getDishItemById(Long id);


    /**
     * 修改套餐
     *
     * @param setmealDTO
     * @return
     */
    void updateSetmeal(SetmealDTO setmealDTO);
}
