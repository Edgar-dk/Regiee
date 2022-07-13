package com.sias.regiee.common;

/**
 * @author Edgar
 * @create 2022-07-13 9:42
 * @faction:
 */


/*一：基于ThreadLocal封装工具类
*    用户保存和获取当前登陆用户的id
*    下面一个是获取id，一个是使用id*/
public class BaseContext {
    private static ThreadLocal<Long> threadLocal =new ThreadLocal<>();

    public static void setCurrent(Long id){
        threadLocal.set(id);
    }
    public static Long getCurrent(){
        return threadLocal.get();
    }
}
