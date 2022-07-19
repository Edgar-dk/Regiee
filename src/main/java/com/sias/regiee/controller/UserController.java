package com.sias.regiee.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.sias.regiee.common.R;
import com.sias.regiee.entity.User;
import com.sias.regiee.service.impl.UserServiceImpl;
import com.sias.regiee.untils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author Edgar
 * @create 2022-07-19 15:52
 * @faction:
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    /*1.发送验证码
    *   需要在阿里上开启发送的业务*/
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user ,HttpSession session){
        String phone = user.getPhone();
        log.info(phone);
        if (StringUtils.isNotEmpty(phone)){
            /*01.随机产生验证码*/
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code:{}",code);
            session.setAttribute(phone,code);
            return R.success("手机验证码短信发送成功");
        }
        return R.success("短信发送失败");
    }

    /*2，登陆方法*/
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map , HttpSession session){
        String phone = map.get("phone").toString();
        String code = map.get("code").toString();
        Object codeSession = session.getAttribute(phone);
        if (codeSession != null && codeSession.equals(code)){
            /*01.如果对比成功的话，表示
            *    验证码对比成功，这个时候，就需要去验证
            *    用户是否存在，不存在的话，注册到数据库中*/
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getPhone,phone);
            /*02.从数据库中查询数据，有的话，把
            *    查询到的数据，返回给前端，前端做出
            *    一定的处理，没有的话在把数据插入数据库*/
            User user = userService.getOne(wrapper);
            if (user==null){
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            return R.success(user);
        }
        return R.error("登陆失败");
    }
}

