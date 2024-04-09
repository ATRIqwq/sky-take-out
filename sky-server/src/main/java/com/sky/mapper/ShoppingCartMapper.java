package com.sky.mapper;

import com.sky.entity.ShoppingCart ;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author 86136
* @description 针对表【shopping_cart(购物车)】的数据库操作Mapper
* @createDate 2024-04-09 15:17:08
* @Entity generator.domain.ShoppingCart
*/

@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {

    List<ShoppingCart> list(ShoppingCart shoppingCart);
}




