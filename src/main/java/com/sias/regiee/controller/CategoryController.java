package com.sias.regiee.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sias.regiee.common.R;
import com.sias.regiee.entity.Category;
import com.sias.regiee.entity.Employee;
import com.sias.regiee.service.CategoryService;
import com.sias.regiee.service.impl.CategoryServiceImpl;
import com.sias.regiee.service.impl.EmployServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author Edgar
 * @create 2022-07-10 9:09
 * @faction:
 */
@Slf4j
@RestController
@RequestMapping("/category")//在下面的所有方法前面加上这个地址
public class CategoryController {

    @Autowired
    private CategoryServiceImpl categoryService;

    /*1.添加新菜品*/
    @PostMapping
    public R<String> save(@RequestBody Category category){
        boolean save = categoryService.save(category);
        if (save!=true){
            R.error("数据错误");
        }
        return R.success("插入成功");
    }


    /*2.分页请求*/
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize){
        /*01.构造分页构造器
         *    就是把条件传递进去*/
        Page pageInfo =new Page(page,pageSize);

        /*02.查询出来的数据都在pageInfo中，把这里面的数据返回给浏览器
        *    然后按照ElementUi去渲染数据*/
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Category::getSort);

        Page page1 = categoryService.page(pageInfo, wrapper);
        return R.success(pageInfo);
    }

    /*3.删除菜品*/
    @DeleteMapping
    public R<String> delete(Long ids){
        /*boolean removeById = categoryService.removeById(id);
        if (removeById!=true){
            R.error("删除失败");
        }*/

        /*01.看看是否关联了菜品和套餐*/
        categoryService.removeAL(ids);
        return R.success("删除成功了++++++");
    }

    /*4.修改菜品的名字*/
    @PutMapping
    public R<String> updateById(@RequestBody Category category){
        boolean updateById = categoryService.updateById(category);
        if (updateById!=true){
            return R.error("修改失败");
        }
        return R.success("修改成功");
    }

}
