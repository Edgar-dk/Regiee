package com.sias.regiee.dto;

import com.sias.regiee.entity.Dish;
import com.sias.regiee.entity.DishFlavor;
import com.sias.regiee.entity.Dish;
import com.sias.regiee.entity.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
