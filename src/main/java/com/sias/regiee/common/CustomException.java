package com.sias.regiee.common;

/**
 * @author Edgar
 * @create 2022-07-13 15:31
 * @faction:
 */


/*一：这是一个运行时产生的异常
*    在代码的部分会加这个类，把
*    错误的信息抛向全局异常类里面*/
public class CustomException extends RuntimeException {
    public CustomException (String message){
        super(message);
    }
}
