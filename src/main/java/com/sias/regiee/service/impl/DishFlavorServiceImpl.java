package com.sias.regiee.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sias.regiee.common.CustomException;
import com.sias.regiee.entity.Category;
import com.sias.regiee.entity.Dish;
import com.sias.regiee.entity.DishFlavor;
import com.sias.regiee.entity.Setmeal;
import com.sias.regiee.mapper.CategoryMapper;
import com.sias.regiee.mapper.DishFlavorMapper;
import com.sias.regiee.service.CategoryService;
import com.sias.regiee.service.DishFlavorService;
import com.sias.regiee.service.DishService;
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
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {


}
