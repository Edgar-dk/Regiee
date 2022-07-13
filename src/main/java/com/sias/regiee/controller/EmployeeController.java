package com.sias.regiee.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sias.regiee.common.R;
import com.sias.regiee.entity.Employee;
import com.sias.regiee.service.EmployeeService;
import com.sias.regiee.service.impl.EmployServiceImpl;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nls;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Edgar
 * @create 2022-07-10 9:09
 * @faction:
 */
@Slf4j
@RestController
@RequestMapping("/employee")//在下面的所有方法前面加上这个地址
public class EmployeeController {

    @Resource
    private EmployServiceImpl employService;


    /*1.首页面登陆功能*/
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        /*01.将页面提交过来的密码md5加密*/
        String password = employee.getPassword();
        String passwordL = DigestUtils.md5DigestAsHex(password.getBytes());
        /*02.使用MybatisPlus封装好的代码查询*/
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        /*001.getOne是查询数据库中唯一的索引，速度是很快的*/
        Employee emp = employService.getOne(queryWrapper);
        /*03.如果没有查询出，返回登陆失败
        *    没有数据的话，不会往下面执行*/
        if (emp==null){
            return R.error("登陆失败");
        }
        /*04.密码不一致登陆失败*/
        if (!emp.getPassword().equals(passwordL)){
            return R.error("登陆失败");
        }
        /*05.查询员工状态*/
        if (emp.getStatus()==0){
            return R.error("账号已禁用");
        }
        /*06.将数据放在session中*/
        HttpSession session = request.getSession();
        session.setAttribute("employee",emp.getId());
        /*07.根据用户名去查询出来的用户
        *    数据，把这个数据返回给浏览器
        *    需要用到这个详细的数据*/
        return R.success(emp);
    }

    /*2.首页面的退出功能*/
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }


    /*3.新增员工信息*/
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee){
        /*01.{}是占位符，可以在获取信息的时候
        *    把信息填写在这个占位符的位置上*/
        log.info("新增员工的信息:{}",employee.toString());
        /*02.设置修改时间以及修改者
        *    还有更新时间，还有设置密码
        *    注意：employee是员工，可以在后台去添加员工的基本信息
        *    员工的userName是唯一的约束，不可以为空，也不可以有相同的
        *    前段没有输入密码的话，需要后端去写上密码*/
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        /*employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        Long empId = (Long)request.getSession().getAttribute("employee");
        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);*/
        boolean save = employService.save(employee);
        if (save!=true){
            return R.error("新增员工失败");
        }
        return R.success("新增员工信息成功");
    }


    /*4.分页查询*/
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        log.info("page={},pageSize={},name={}",page,pageSize,name);
        /*01.构造分页构造器
        *    就是把条件传递进去*/
        Page pageInfo =new Page(page,pageSize);
        /*02.添加过滤条件
        *    条件就是传递的name,去按照日期降序的方式
        *    去排列数据*/
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name)
                .orderByDesc(Employee::getUpdateTime);

        Page page1 = employService.page(pageInfo, wrapper);
        return R.success(pageInfo);
    }


    /*5.更新操作*/
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
        /*Long empId = (Long) request.getSession().getAttribute("employee");

        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(empId);*/
        boolean updateById = employService.updateById(employee);

        if (updateById!=true){
            return R.error("执行失败");
        }
        log.info(employee.toString());
        return R.success("员工信息修改成功");
    }

    /*6.编辑员工信息*/
    @GetMapping("/{id}")
    public R<Employee> updateById(@PathVariable Long id){
        Employee serviceById = employService.getById(id);
        if(serviceById!=null){
            return R.success(serviceById);
        }
        return R.error("没有查询到信息");
    }

}
