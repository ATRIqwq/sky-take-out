package com.sky.controller.user;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/addressBook")
@Api(tags = "用户收货地址相关操作")
@Slf4j
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    /**
     * 新增用户地址
     * @param addressBook
     * @return
     */
    @PostMapping
    @ApiOperation("新增用户地址")
    public Result addAddress(@RequestBody AddressBook addressBook){
        Long userId = BaseContext.getCurrentId();
        addressBook.setUserId(userId);
        addressBookService.save(addressBook);
        return Result.success();
    }


    /**
     * 查询用户所有地址
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("查询用户所有地址")
    public Result<List<AddressBook>> listAddress(){
        List<AddressBook> list = addressBookService.list();
        return Result.success(list);
    }

    /**
     * 查询默认收货地址
     * @return
     */
    @GetMapping("/defaut")
    @ApiOperation("查询默认地址")
    public Result<AddressBook> listDefaut(){
        QueryWrapper<AddressBook> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(AddressBook::getIsDefault,1);
        List<AddressBook> list = addressBookService.list(wrapper);
        if (list != null && list.size() >0){
            AddressBook addressBook = list.get(0);
            return Result.success(addressBook);
        }
        return Result.error("没有默认收货地址");
    }

    /**
     * 设置默认地址
     *
     * @param addressBook
     * @return
     */
    @PutMapping("/default")
    @ApiOperation("设置默认地址")
    public Result setDefault(@RequestBody AddressBook addressBook) {
        addressBookService.setDefault(addressBook);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询地址")
    public Result<AddressBook> getById(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        return Result.success(addressBook);
    }

    /**
     * 根据id修改地址
     *
     * @param addressBook
     * @return
     */
    @PutMapping
    @ApiOperation("根据id修改地址")
    public Result update(@RequestBody AddressBook addressBook) {
        addressBookService.updateById(addressBook);
        return Result.success();
    }

    /**
     * 根据id删除地址
     *
     * @param id
     * @return
     */
    @DeleteMapping
    @ApiOperation("根据id删除地址")
    public Result deleteById(Long id) {
        addressBookService.removeById(id);
        return Result.success();
    }




}
