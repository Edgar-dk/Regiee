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


    /*2.菜品的分页查询*/
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
        //对象拷贝,从dish中查询表的附加信息（多少行，多少数据）给dishDtoPage，关于records（表的真实数据）是关于
        //dish表中的数据，尽管是可以直接给dishDto，需要把dish中的catoryid和dishdto中的categoryName对应上
        //关于dish总的categoryId怎么和categoryName对应上，需要按照这个id去category中查询出来，把数据放在
        //category实体类中，然后在把category里面name的值复制给categoryName，多少个id就要去查询多少次，然后
        //在赋值categoryName多少次，这么多的次数，需要用遍历的方式去做到，关于在DishDto中categoryId的数据，还是
        //存在的，最终把数据给DishDtoPage，需要和表的附属值一块传递给前端的，尽管前端不会把全部的数据使用完，还是使用
        //一部分的，另外一部分数据存储在内存中，在想要使用的时候，直接按照变量的值去取数据就可以了
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);
            /*前端在数据处理的时候，需要用到categoryName，所以需要设置好，在封装在DishDtoPage中
            * 一块放回给页面*/
            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(list);
        return R.success(dishDtoPage);
    }

    /*3.菜品的修改*/
    @GetMapping("/{id}")
    public R<DishDto> upload(@PathVariable Long id){
        DishDto dishDto = dishService.selectTables(id);
        return R.success(dishDto);
    }

    /*4.更新菜品信息还有口味信息*/
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        dishService.updateWithFlavor(dishDto);
        return R.success("保存数据成功");
    }


    /*5.根据条件
    查询对应的菜品信息*/
    @GetMapping("/list")
    public R<List<Dish>> selectList(Dish dish){
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId())
                .eq(Dish::getStatus,1)
                .orderByAsc(Dish::getSort)
                .orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(wrapper);
        return R.success(list);
    }

}
