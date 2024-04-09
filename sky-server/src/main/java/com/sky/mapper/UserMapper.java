package com.sky.mapper;

import com.sky.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 86136
* @description 针对表【user(用户信息)】的数据库操作Mapper
* @createDate 2024-04-08 16:06:21
* @Entity generator.domain.User
*/

@Mapper
public interface UserMapper extends BaseMapper<User> {

}




