package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal ;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.mapper.SetmealMapper;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
* @author 86136
* @description 针对表【setmeal(套餐)】的数据库操作Service实现
* @createDate 2024-04-03 19:58:48
*/
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal>
    implements SetmealService{

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private DishMapper dishMapper;


    @Override
    public void saveWithDish(SetmealDTO setmealDTO) {
        //添加套餐基本信息，设置默认状态，创建时间和创建人
        Setmeal setmeal = new Setmeal();
        Long currentId = BaseContext.getCurrentId();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmeal.setCreateTime(LocalDateTime.now());
        setmeal.setCreateUser(currentId);
        setmeal.setUpdateTime(LocalDateTime.now());
        setmeal.setUpdateUser(currentId);
        setmeal.setStatus(StatusConstant.DISABLE);
        setmealMapper.insert(setmeal);

        Long setmealId = setmeal.getId();

        //在套餐包含的菜品表中添加套餐ID
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> {
            setmealDish.setSetmealId(setmealId);
            setmealDishMapper.insert(setmealDish);
        });

    }

    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        //判断改套餐是否启用
        for (Long id : ids) {
            Setmeal setmeal = setmealMapper.selectById(id);
            if (setmeal.getStatus() == StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }

        }

        //删除套餐表中的数据
        setmealMapper.deleteBatchIds(ids);

        //删除套餐菜品表中的数据
        setmealDishMapper.deleteBatchIds(ids);

    }

    @Override
    public SetmealVO listWithDish(Long id) {
        //根据ID查询套餐表中的信息
        Setmeal setmeal = setmealMapper.selectById(id);
        Long categoryId = setmeal.getCategoryId();
        Long setmealId = setmeal.getId();


        //查询分类表name
        Category category = categoryMapper.selectById(categoryId);
        String categoryName = category.getName();

        //查询setmeal_dish表
        QueryWrapper<SetmealDish> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SetmealDish::getSetmealId,setmealId);
        List<SetmealDish> setmealDishes = setmealDishMapper.selectList(wrapper);

        //封装成VO对象返回
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal,setmealVO);
        setmealVO.getCategoryName();
        setmealVO.setSetmealDishes(setmealDishes);

        return setmealVO;
    }

    @Override
    public void startOrStop(Integer status, Long id) {
        if (status == StatusConstant.ENABLE){
            QueryWrapper<SetmealDish> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(SetmealDish::getSetmealId,id);
            List<SetmealDish> setmealDishes = setmealDishMapper.selectList(wrapper);

            setmealDishes.forEach(setmealDish -> {
                Long dishId = setmealDish.getDishId();
                Dish dish = dishMapper.selectById(dishId);
                if (dish != null){
                    if (dish.getStatus() == StatusConstant.DISABLE){
                        throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                    }
                }

            });
        }

        Setmeal setmeal = new Setmeal();
        setmeal.setStatus(status);
        setmeal.setId(id);
        setmealMapper.updateById(setmeal);

    }


    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    public List<Setmeal> listSetmeal(Setmeal setmeal) {
        List<Setmeal> list = setmealMapper.listSetmeal(setmeal);
        return list;
    }

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealMapper.getDishItemBySetmealId(id);
    }

    @Override
    public void updateSetmeal(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);

        Long currentId = BaseContext.getCurrentId();
        setmeal.setUpdateTime(LocalDateTime.now());
        setmeal.setUpdateUser(currentId);

        setmealMapper.updateById(setmeal);



        Long setmealId = setmealDTO.getId();
        QueryWrapper<SetmealDish> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SetmealDish::getSetmealId,setmealId);
        setmealDishMapper.delete(wrapper);

        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmealId);
            setmealDishMapper.insert(setmealDish);
        }



    }

}




