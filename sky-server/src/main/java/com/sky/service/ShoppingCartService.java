package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart ;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 86136
* @description 针对表【shopping_cart(购物车)】的数据库操作Service
* @createDate 2024-04-09 15:17:08
*/
public interface ShoppingCartService extends IService<ShoppingCart> {

    /**
     * 添加商品到购物车
     * @param shoppingCartDTO
     * @return
     */
    void add(ShoppingCartDTO shoppingCartDTO);

    /**
     * 查看购物车
     * @return
     */
    List<ShoppingCart> listItem();
}
