package com.sias.regiee.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sias.regiee.common.R;
import com.sias.regiee.dto.DishDto;
import com.sias.regiee.entity.Category;
import com.sias.regiee.entity.Dish;
import com.sias.regiee.service.impl.CategoryServiceImpl;
import com.sias.regiee.service.impl.DishFlavorServiceImpl;
import com.sias.regiee.service.impl.DishServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Edgar
 * @create 2022-07-15 9:11
 * @faction:
 */
@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {
    @Resource
    private DishServiceImpl dishService;
    @Resource
    private DishFlavorServiceImpl dishFlavorService;

    @Resource
    private CategoryServiceImpl categoryService;

    /*1.新增菜品*/
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        dishService.saveWithFlavor(dishDto);
        return R.success("保存数据成功");
    }

    /*2.分页查询菜品*/
/*    @GetMapping("/page")
    public R<Page> selectPage(int page,int pageSize,String name){
       Page<Dish> pageInfo =new Page<>(page,pageSize);
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(name!=null,Dish::getName,name)
                .orderByDesc(Dish::getUpdateTime);
        dishService.page(pageInfo,wrapper);
        return R.success(pageInfo);
    }*/

    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){

        //构造分页构造器对象
        Page<Dish> pageInfo = new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(name != null,Dish::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        //执行分页查询
        dishService.page(pageInfo,queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");

        List<Dish> records = pageInfo.getRecords();

        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);

            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }



}
