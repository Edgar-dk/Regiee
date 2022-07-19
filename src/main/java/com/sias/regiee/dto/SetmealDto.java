package com.sias.regiee.dto;


import com.sias.regiee.entity.Setmeal;
import com.sias.regiee.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
