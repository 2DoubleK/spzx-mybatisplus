package com.atguigu.spzx.manager.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.spzx.exception.GuiguException;
import com.atguigu.spzx.manager.Utils.EasyExcelListener;
import com.atguigu.spzx.manager.mapper.CategoryMapper;
import com.atguigu.spzx.manager.service.CategoryService;
import com.atguigu.spzx.model.dto.product.CategoryExcelVo;
import com.atguigu.spzx.model.entity.product.Category;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Override
    @Transactional(rollbackFor = Exception.class)  //
    public Result findCategoryList(Long id) {
        LambdaQueryWrapper<Category> lq = new LambdaQueryWrapper<>();
        lq.eq(Category::getParentId, id).eq(Category::getIsDeleted, 0); //查询” 顶级 “菜单
        List<Category> categoryList = list(lq);
        //遍历顶级菜单集合，判断是否有设置hasChildren=true，有就继续查它的子菜单
        if (!categoryList.isEmpty()) {
            categoryList.forEach(category -> {
                Long childrenNum = query().eq("parent_id", category.getId()).count();//查询所有子菜单
                if (childrenNum.longValue() > 0) { //有子分类
                    category.setHasChildren(true);
                } else {
                    category.setHasChildren(false);
                }
            });
        }
        return Result.build(categoryList, ResultCodeEnum.SUCCESS);
    }

    //导出excel文件
    @Override
    public Result exportExcel(HttpServletResponse response) {
        try {
            // 1.设置响应结果类型
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("分类数据", "UTF-8");// 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            //response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            //2.查询所有分类返回集合
            List<Category> categoryList = query().list();
            List<CategoryExcelVo> categoryExcelVos = new ArrayList<>();
            for (Category category : categoryList) {
                CategoryExcelVo categoryExcelVo = new CategoryExcelVo();
                BeanUtils.copyProperties(category, categoryExcelVo);
                categoryExcelVos.add(categoryExcelVo);
            }
            //3.调用easyExcel的write的方法
            EasyExcel.write(response.getOutputStream(), CategoryExcelVo.class).sheet("商品分类数据").doWrite(categoryExcelVos);

        } catch (IOException e) {
            throw new GuiguException(ResultCodeEnum.DATA_ERROR);
        }
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result importExcel(MultipartFile file) {
        //创建监听器
        EasyExcelListener easyExcelListener=new EasyExcelListener(categoryMapper);//每次写入都是新创建一个监听器避免了并发问题
        try {
            EasyExcel.read(file.getInputStream(), CategoryExcelVo.class, easyExcelListener).sheet().doRead();

        } catch (IOException e) {
            throw new GuiguException(ResultCodeEnum.DATA_ERROR);
        }
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }
}
