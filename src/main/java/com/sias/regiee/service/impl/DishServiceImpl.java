package com.sias.regiee.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sias.regiee.common.R;
import com.sias.regiee.dto.DishDto;
import com.sias.regiee.entity.Category;
import com.sias.regiee.entity.Dish;
import com.sias.regiee.entity.DishFlavor;
import com.sias.regiee.mapper.CategoryMapper;
import com.sias.regiee.mapper.DishMapper;
import com.sias.regiee.service.CategoryService;
import com.sias.regiee.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /*2.查询菜品和对应的口味信息，显示在页面上*/
    public DishDto selectTables(Long id){

        /*01.按照id去查询菜品信息的话，虽然说可以
        *    传递给DishDto，尽量不要，传递给DIsh
        *    可以使属性里面都有数据，要去给DIshDto
        *    数据的话，可以使用对象拷贝，把DIsh里面的数据
        *    拷贝到DIshDto里面，是按照属性一一对应的
        *    原则去拷贝的*/
        Dish byId = this.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(byId,dishDto);
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId,byId.getId());

        /*02.一个菜品的口味有多条
        *    可以使用list集合的方式去接收数据
        *    最后在把list放在DishDto对象里面
        *    返回给浏览器*/
        List<DishFlavor> list = dishFlavorService.list(wrapper);
        dishDto.setFlavors(list);
        return dishDto;
    }

    /*3.更新菜品信息，以及口味信息*/
    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        boolean updateById = this.updateById(dishDto);

        /*01.构造条件先删除表中的数据，在插入数据*/
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId,dishDto.getId());
        boolean remove = dishFlavorService.remove(wrapper);
        /*02.插入数据、
        *    dishDto.getFlavors();是浏览器过来的数据
        *    一开始保存在dishDto中，通过get的方式去获得
        *    这些口味信息，然后在放在DishFlavor口味的单独
        *    实体类中按照List集合的方式保存数据，然后在为
        *    这些List集合中每一个值，赋值一个id，保存数据
        *    可以按照id来保存在数据库中*/
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors=flavors.stream().map((item)->{
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        /*03.按照id去保存list集合中的信息*/
        dishFlavorService.saveBatch(flavors);
    }
}
