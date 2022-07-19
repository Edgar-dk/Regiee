package com.sias.regiee.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sias.regiee.dto.SetmealDto;
import com.sias.regiee.entity.Category;
import com.sias.regiee.entity.Setmeal;
import com.sias.regiee.entity.SetmealDish;

import java.util.List;

/**
 * @author Edgar
 * @create 2022-07-05 21:16
 * @faction:
 */
public interface SetmealService extends IService<Setmeal> {


    /*1.新增套餐
    *   需要保存套餐和菜品之间的关系*/
    public void saveWithDish(SetmealDto setmealDto);

    /*2.删除套餐*/
    public void deleteWithDish(List<Long> ids);
}
