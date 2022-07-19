package com.sias.regiee.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sias.regiee.common.CustomException;
import com.sias.regiee.dto.SetmealDto;
import com.sias.regiee.entity.Category;
import com.sias.regiee.entity.Setmeal;
import com.sias.regiee.entity.SetmealDish;
import com.sias.regiee.mapper.CategoryMapper;
import com.sias.regiee.mapper.SetmealMapper;
import com.sias.regiee.service.CategoryService;
import com.sias.regiee.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Edgar
 * @create 2022-07-05 21:16
 * @faction:
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {


    @Autowired
    private SetmealDishServiceImpl setmealDishService;

    /*1.新增套餐
     *   需要保存套餐和菜品之间的关系*/
    public void saveWithDish(SetmealDto setmealDto) {

        /*01.在保存菜品的时候，会去创建一个套餐的id
        *    id创建好之后，在把这个id在下面使用
        *    save是保存，就是插入的意思*/
        this.save(setmealDto);


        /*02.为每一个套餐和菜品之间的关系
        *    保存再来，可以在以后使用，在去保存
        *    这个关系的时候，套餐的id没有，所以
        *    需要上面先执行，产生一个id之后，在
        *    去到关联的表中，把这个id赋值给这个表*/
        List<SetmealDish> dishes = setmealDto.getSetmealDishes();
        dishes = dishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        /**/
        setmealDishService.saveBatch(dishes);
    }

    /*2.删除套餐*/
    @Override
    public void deleteWithDish(List<Long> ids) {

        /*01.先去删除套餐表
        *    eq是等值查询，in是包含多个参数*/
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Setmeal::getId,ids)
                .eq(Setmeal::getStatus,1);
        long count = this.count(wrapper);
        if (count>0){
            throw new CustomException("套餐正在售卖中，不可以删除");
        }else {
            this.removeByIds(ids);
        }
        /*02.在删除套餐对应的菜品表*/
        LambdaQueryWrapper<SetmealDish> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(wrapper1);
    }
}
