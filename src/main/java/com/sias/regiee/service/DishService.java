package com.sias.regiee.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sias.regiee.common.R;
import com.sias.regiee.dto.DishDto;
import com.sias.regiee.entity.Category;
import com.sias.regiee.entity.Dish;

/**
 * @author Edgar
 * @create 2022-07-05 21:16
 * @faction:
 */
public interface DishService extends IService<Dish> {

    /*1.查询菜品分类*/
    public void saveWithFlavor(DishDto dishDto);

    /*2.查询菜品*/
    public DishDto selectTables(Long id);

    public void updateWithFlavor(DishDto dishDto);
}
