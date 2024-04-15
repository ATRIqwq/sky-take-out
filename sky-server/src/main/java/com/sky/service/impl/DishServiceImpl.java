package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.mapper.DishMapper;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.time.LocalDateTime.now;

/**
* @author 86136
* @description 针对表【dish(菜品)】的数据库操作Service实现
* @createDate 2024-04-03 12:27:18
*/
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish>
    implements DishService{

    @Autowired
    private DishMapper dishMapper;

    @Resource
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    @Transactional
    public void save(DishDTO dishDTO) {

        //向菜品表中插入一条数据
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        Long userId = BaseContext.getCurrentId();
        dish.setCreateUser(userId);
        dish.setUpdateUser(userId);
        dish.setCreateTime(now());
        dish.setUpdateTime(now());
        dishMapper.insert(dish);
        Long dishId = dish.getId();

        //向口味表中插入n条数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors != null && flavors.size() > 0){
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
                dishFlavorMapper.insert(dishFlavor);
            });
        }


        


    }

    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());


    }

    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        //判断菜品是否启用
        for (Long id : ids) {
            Dish dish = dishMapper.selectById(id);
            if (dish.getStatus() == StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }


        ArrayList<Long> setmealIds = new ArrayList<>();
        //判断菜品是否关联套餐
        List<SetmealDish> setmealDishes = setmealDishMapper.selectBatchIds(ids);
        setmealDishes.forEach(setmealDish -> {
            setmealIds.add(setmealDish.getSetmealId());
        });

        if (setmealIds != null && setmealIds.size() > 0){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        //删除菜品，以及菜品口味
//        for (Long id : ids) {
//            dishMapper.deleteById(id);
//            dishFlavorMapper.deleteById(id);
//        }

        dishMapper.deleteBatchIds(ids);
        dishFlavorMapper.deleteBatchIds(ids);

    }

    /**
     * 更新ID查询菜品，用于回显
     * @return
     */
    @Override
    public DishVO getDishWithFavor(Long id) {
        //查询菜品基本信息
        Dish dish = dishMapper.selectById(id);

        //查询口味信息
        QueryWrapper<DishFlavor> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(DishFlavor::getDishId,id);
        List<DishFlavor> dishFlavors = dishFlavorMapper.selectList(wrapper);

        //组装成VO返回
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish,dishVO);
        dishVO.setFlavors(dishFlavors);

        return dishVO;
    }

    @Override
    public void updateDishWithFavor(DishDTO dishDTO) {
        //修改菜品基本信息
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        Long currentId = BaseContext.getCurrentId();
        dish.setUpdateUser(currentId);
        dish.setUpdateTime(now());

        dishMapper.updateById(dish);

        Long dishId = dish.getId();

        //删除原来口味
        QueryWrapper<DishFlavor> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(DishFlavor::getDishId,dishId);
        dishFlavorMapper.delete(wrapper);

        //保存修改后的口味
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors !=null && flavors.size() >0){
            for (DishFlavor flavor : flavors) {
                flavor.setDishId(dishId);
                dishFlavorMapper.insert(flavor);
            }
        }




    }

    @Override
    public List<Dish> listById(Long categoryId) {


        Dish dish = new Dish();
        QueryWrapper<Dish> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Dish::getStatus,StatusConstant.ENABLE)
                .eq(Dish::getCategoryId,categoryId);
        List<Dish> list = this.list(wrapper);
        return list;
    }

    @Override
    public void updateStatus(Integer status, Long id) {
        Dish dish = new Dish();
        dish.setStatus(status);
        dish.setId(id);
        this.updateById(dish);
    }


    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    public List<DishVO> listWithFlavor(Dish dish) {
        Long categoryId = dish.getCategoryId();
        List<Dish> dishList = listById(categoryId);


        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            Long id = d.getId();
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d, dishVO);

            //根据菜品id查询对应的口味
            QueryWrapper<DishFlavor> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(DishFlavor::getDishId, id);
            List<DishFlavor> dishFlavors = dishFlavorMapper.selectList(wrapper);

            dishVO.setFlavors(dishFlavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;

    }
}




