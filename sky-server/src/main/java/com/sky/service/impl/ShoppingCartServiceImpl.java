package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart ;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.service.ShoppingCartService;
import com.sky.mapper.ShoppingCartMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static java.time.LocalDateTime.now;

/**
* @author 86136
* @description 针对表【shopping_cart(购物车)】的数据库操作Service实现
* @createDate 2024-04-09 15:17:08
*/
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart>
    implements ShoppingCartService{

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;


    /**
     * 添加商品到购物车
     * @param shoppingCartDTO
     * @return
     */
    @Override
    public void add(ShoppingCartDTO shoppingCartDTO) {
        //查询自己的购物车
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);


        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);

        //判断商品是否存在购物车中
        if (list !=null && list.size()>0){
            //存在的话，数量加1
            ShoppingCart cart = list.get(0);
            Integer number = cart.getNumber();
            cart.setNumber(number + 1);

            shoppingCartMapper.updateById(cart);
        } else {
            //不存在的话，插入数据库，数量就是1

            //判断是菜品，还是套餐
            Long setmealId = shoppingCart.getSetmealId();
            Long dishId = shoppingCart.getDishId();
            //是菜品
            if (dishId != null){
                Dish dish = dishMapper.selectById(dishId);
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
            }
            //是套餐
            else {
                Setmeal setmeal = setmealMapper.selectById(setmealId);
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(now());
            shoppingCartMapper.insert(shoppingCart);

        }

    }

    @Override
    public List<ShoppingCart> listItem() {

        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(userId);
        List<ShoppingCart> cartList = shoppingCartMapper.list(shoppingCart);
        return cartList;
    }

    /**
     * 删除购物车中单个数据
     * @param shoppingCartDTO
     * @return
     */
    @Override
    public void deleteItem(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);

        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        if (list != null && list.size() >0){
            ShoppingCart item = list.get(0);

            Integer number = item.getNumber();

            if (number == 1){
                shoppingCartMapper.deleteById(item.getId());
            } else {
                item.setNumber(item.getNumber() -1);
                shoppingCartMapper.updateById(item);
            }

        }





    }

    /**
     * 清空购物车
     * @return
     */
    @Override
    public void clear() {
        Long userId = BaseContext.getCurrentId();

        QueryWrapper<ShoppingCart> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(ShoppingCart::getUserId,userId);
        shoppingCartMapper.delete(wrapper);

    }
}




