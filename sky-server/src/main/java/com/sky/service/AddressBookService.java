package com.sky.service;

import com.sky.entity.AddressBook ;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 86136
* @description 针对表【address_book(地址簿)】的数据库操作Service
* @createDate 2024-04-09 17:50:10
*/
public interface AddressBookService extends IService<AddressBook> {

    void setDefault(AddressBook addressBook);
}
