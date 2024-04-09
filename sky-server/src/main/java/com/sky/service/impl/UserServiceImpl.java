package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User ;
import com.sky.exception.LoginFailedException;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.mapper.UserMapper;
import com.sky.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;

import static java.time.LocalDateTime.now;

/**
* @author 86136
* @description 针对表【user(用户信息)】的数据库操作Service实现
* @createDate 2024-04-08 16:06:22
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{
    @Autowired
    private WeChatProperties weChatProperties;

    @Autowired
    private UserMapper userMapper;
    //微信服务接口地址
    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";

    @Override
    public User wxLogin(UserLoginDTO userLoginDTO) {

        String openid = getOpenid(userLoginDTO.getCode());

        //判断openID是否合法
        if (openid == null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        //判断是否是新用户
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(User::getOpenid,openid);
        User user = userMapper.selectOne(wrapper);

        //自动注册，将新用户插入数据库
        if (user == null){
            User userNew = new User();
            userNew.setOpenid(openid);
            userNew.setCreateTime(now());
            userMapper.insert(userNew);

            return userNew;
        }

        //返回User
        return user;
    }

    private String getOpenid(String code){
        //调用微信服务获取openID
        HashMap<String, String> map = new HashMap<>();
        map.put("appid",weChatProperties.getAppid());
        map.put("secret",weChatProperties.getSecret());
        map.put("js_code",code);
        map.put("grant_type","authorization_code");
        String json = HttpClientUtil.doGet(WX_LOGIN, map);
        JSONObject jsonObject = JSON.parseObject(json);
        String openid = jsonObject.getString("openid");
        return openid;
    }
}




