package com.sias.regiee.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sias.regiee.common.CustomException;
import com.sias.regiee.entity.Category;
import com.sias.regiee.entity.Dish;
import com.sias.regiee.entity.Employee;
import com.sias.regiee.entity.Setmeal;
import com.sias.regiee.mapper.CategoryMapper;
import com.sias.regiee.mapper.EmployeeMapper;
import com.sias.regiee.service.CategoryService;
import com.sias.regiee.service.DishService;
import com.sias.regiee.service.EmployeeService;
import com.sias.regiee.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Edgar
 * @create 2022-07-05 21:16
 * @faction:
 */
@Service
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    /*菜品加进来*/
    @Autowired
    private DishService dishService;
    /*套餐加进来*/
    @Autowired
    private SetmealService setmealService;

    /*1.自己定义方法按照id去删除
    *   删除之前看看是否关联了菜品，以及套餐，关联的话，向上面抛出一个异常*/
    @Override
    public void removeAL(Long ids) {

        /*01.去菜品里面查询是否关联*/
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId,ids);
        long count = dishService.count(queryWrapper);
        if (count>0){
            log.info("关联了菜品");
            throw new CustomException("关联了菜品，不能删除");
        }
        /*02.看看是否关联了
        *    只有这两个都没有完成的话，才执行删除的语句*/
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Setmeal::getCategoryId,ids);
        long count1 = setmealService.count(wrapper);
        if (count1>0){
            log.info("关联了套餐");
            throw new CustomException("关联了套餐，不能删除");
        }
        super.removeById(ids);
    }
}
