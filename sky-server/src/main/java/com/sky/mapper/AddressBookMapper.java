package com.sky.mapper;

import com.sky.entity.AddressBook;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
* @author 86136
* @description 针对表【address_book(地址簿)】的数据库操作Mapper
* @createDate 2024-04-09 17:50:10
* @Entity generator.domain.AddressBook
*/

@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {

    /**
     * 根据 用户id修改 是否默认地址
     * @param addressBook
     */
    @Update("update address_book set is_default = #{isDefault} where user_id = #{userId}")
    void updateIsDefaultByUserId(AddressBook addressBook);
}




