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
import java.util.List;

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

        categoryService.page(pageInfo, wrapper);
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


    /*5.根据条件查询菜品分类*/
    @GetMapping("/list")
    public R<List<Category>> selectList(Category category){
        /*01.先把数据封装在category里面
        *    可以为以后操作数据方便，构造条件的时候
        *    先去按照Type去升序的排列，Type一样的话
        *    在去按照更新的时间排列*/
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(category.getType()!=null,Category::getType,category.getType())
                .orderByAsc(Category::getType)
                .orderByDesc(Category::getUpdateTime);
        /*02.查询到的数据，按照集合的形式返回给前端
        *    前端收到数据之后，间接性处理数据，有的
        *    数据需要去处理，有的不需要处理
        *    注意：应该说在数据库中查询到的数据全部返回给
        *    前端，前端按照需求处理这些数据*/
        List<Category> list = categoryService.list(wrapper);
        return R.success(list);
    }

}
