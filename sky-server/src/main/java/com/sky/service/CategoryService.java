package com.sky.service;

import com.sky.annotation.AutoFill;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.enumeration.OperationType;
import com.sky.result.PageResult;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 86136
* @description 针对表【category(菜品及套餐分类)】的数据库操作Service
* @createDate 2024-03-26 20:04:08
*/

@Service
public interface CategoryService extends IService<Category> {

//    @AutoFill(value = OperationType.INSERT)
    void saveCategory(CategoryDTO categoryDTO);

    PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

//    @AutoFill(value = OperationType.UPDATE)
    void updateCategory(CategoryDTO categoryDTO);

    /**
     * 修改分类状态
     * @param status
     * @param id
     */
    void updateStatus(Integer status, Long id);

    /**
     * 根据类型查询分类
     * @param type
     * @return
     */
    List<Category> list(Integer type);
}
