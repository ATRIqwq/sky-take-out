package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee ;
import com.sky.result.PageResult;

/**
* @author 86136
* @description 针对表【employee(员工信息)】的数据库操作Service
* @createDate 2024-03-25 21:10:33
*/
public interface EmployeeService  extends IService<Employee> {
    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
   Employee login(EmployeeLoginDTO employeeLoginDTO);


    /**
     * 新增员工
     * @param employeeDTO
     */
    void save(EmployeeDTO employeeDTO);

    /**
     * 分页查询员工
     * @param employeePageQueryDTO
     * @return
     */

    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    void startOrStop(Integer status, Long id);
}
