package com.sias.regiee.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @author Edgar
 * @create 2022-07-12 10:10
 * @faction:
 */

/*一：只要是下面注解使用的类，出现异常的话，都会去拦截
*     里面封装了ResponseBody用来返回JSON得到的结果
*     目的：设置全局异常总线，出现异常，都会执行这个类
*     的方法*/
@RestControllerAdvice(annotations = {RestController.class, Controller.class})
@Slf4j
public class GlobalException {

    /*01.问题本身产生的异常*/
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exception(SQLIntegrityConstraintViolationException exception){
        log.error("++++++++++++++++++++++++"+exception.getMessage());
        /*01.exception.getMessage().contains是看消息里面是否
        *    包含括号里面的信息，包含的话，继续往下面执行，split是
        *    按照空格的形式把字符拆分，然后在取出里面的一个字符*/
        if (exception.getMessage().contains("Duplicate entry")){
            String[] split = exception.getMessage().split(" ");
            String msg = split[2]+"已存在";
            return R.error(msg);
        }
        return R.error("未知错误");
    }


    /*02.自定义的异常处理类*/
    @ExceptionHandler(CustomException.class)
    public R<String> CustomException(CustomException exception){
        log.error("++++++++++++++++++++++++"+exception.getMessage());

        return R.error(exception.getMessage());
    }
}

