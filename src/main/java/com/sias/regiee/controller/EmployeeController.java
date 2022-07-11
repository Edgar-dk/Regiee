package com.sias.regiee.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sias.regiee.common.R;
import com.sias.regiee.entity.Employee;
import com.sias.regiee.service.impl.EmployServiceImpl;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author Edgar
 * @create 2022-07-10 9:09
 * @faction:
 */
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

}
