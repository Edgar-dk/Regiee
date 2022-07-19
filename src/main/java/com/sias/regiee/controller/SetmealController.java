package com.sias.regiee.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sias.regiee.common.R;
import com.sias.regiee.dto.SetmealDto;
import com.sias.regiee.entity.Category;
import com.sias.regiee.entity.Setmeal;
import com.sias.regiee.entity.SetmealDish;
import com.sias.regiee.service.SetmealDishService;
import com.sias.regiee.service.impl.CategoryServiceImpl;
import com.sias.regiee.service.impl.SetmealDishServiceImpl;
import com.sias.regiee.service.impl.SetmealServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Edgar
 * @create 2022-07-17 15:36
 * @faction:
 */

/*一：套餐管理信息*/
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealServiceImpl setmealService;
    @Autowired
    private CategoryServiceImpl categoryService;
    @Autowired
    private SetmealDishServiceImpl setmealDishService;


    /*1.添加套餐*/
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){

        setmealService.saveWithDish(setmealDto);
        return R.success("新增套餐成功");

    }

    /*2.分页查询套餐信息*/
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        Page<SetmealDto> doPage = new Page<>(page,pageSize);
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(name!=null,Setmeal::getName,name)
                .orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(pageInfo,wrapper);
        BeanUtils.copyProperties(pageInfo,doPage,"records");
        List<Setmeal> records = pageInfo.getRecords();
        /*01.单个的需要去一一的赋值上套餐分类
        *    然后在放在分页查询的数据对象中，一并返回给前端
        *    前端处理数据的时候，也是按照这种分页对象数据的方式
        *    处理*/
        List<SetmealDto> list=records.stream().map((items)->{
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(items,setmealDto);
            Long categoryId = items.getCategoryId();
            Category byId = categoryService.getById(categoryId);
            if (byId!=null){
                String name1 = byId.getName();
                setmealDto.setCategoryName(name1);
            }
            return setmealDto;
        }).collect(Collectors.toList());
        doPage.setRecords(list);
        return R.success(doPage);
    }

    /*3.删除套餐*/
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        setmealService.deleteWithDish(ids);
        return R.success("删除数据成功");
    }
}
