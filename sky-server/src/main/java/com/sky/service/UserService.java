package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User ;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 86136
* @description 针对表【user(用户信息)】的数据库操作Service
* @createDate 2024-04-08 16:06:22
*/
public interface UserService extends IService<User> {

    User wxLogin(UserLoginDTO userLoginDTO);
}
