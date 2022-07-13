package com.sias.regiee.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author Edgar
 * @create 2022-07-13 8:15
 * @faction:
 */


/*一：这是一个自定义的元数据处理器
*    当执行到Employee一些字段的时候，每次
*    都要去设置数据，使用这个类，可以动态的
*    设置数据，在这个类中设置好数据了，获取
*    Employee的时候，就是执行这个，得到数据
*    给Employee，然后把数据存储到数据库中*/
@Component
public class MyDateObjectHandle implements MetaObjectHandler {



    /*1.每次执行插入的时候，关于这个字段的所需要的数据，自动执行
    *   然后放在属性当中*/
    @Override
    public void insertFill(MetaObject metaObject) {
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("createUser", BaseContext.getCurrent());
        metaObject.setValue("updateUser", BaseContext.getCurrent());
    }

    /*2.更新的时候，自动执行，去获取数据给属性赋值
    *   然后插入到数据库中*/
    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", BaseContext.getCurrent());
    }
}
