package com.sias.regiee.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sias.regiee.dto.DishDto;
import com.sias.regiee.entity.Category;
import com.sias.regiee.entity.Dish;
import com.sias.regiee.entity.DishFlavor;
import com.sias.regiee.mapper.CategoryMapper;
import com.sias.regiee.mapper.DishMapper;
import com.sias.regiee.service.CategoryService;
import com.sias.regiee.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Edgar
 * @create 2022-07-05 21:16
 * @faction:
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorServiceImpl dishFlavorService;


    /*1.新增菜品的时候，需要操作多张表
    *   因此把上面的口味Service注入了
    *   进来*/
    public void saveWithFlavor(DishDto dishDto) {
        /*01.this指的是DishServiceImpl*/
        this.save(dishDto);
        /*02.设计的表是自增的
        *    数据是在数据库按照雪花算法
        *    来自增数据，上面的是保存数据
        *    最终会吧数据返回给dishDto，然后在从里面
        *    获取id，赋值给口味，因为每一个菜的口味都
        *    不一样，所以需要把id传递给这个口味，在把
        *    口味存储到数据库中*/
        Long dishDtoId = dishDto.getId();

        List<DishFlavor> flavors = dishDto.getFlavors();

        /*03.口味有多个，需要遍历的保存id*/
        flavors=flavors.stream().map((item)->{
            item.setDishId(dishDtoId);
            return item;
        }).collect(Collectors.toList());

        /*04.saveBath是按照list集合的形式保存数据*/
        dishFlavorService.saveBatch(flavors);
    }
}
