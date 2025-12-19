package com.atguigu.spzx.manager.service;

import com.atguigu.spzx.model.entity.product.Category;
import com.atguigu.spzx.model.vo.common.Result;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

public interface CategoryService extends IService<Category> {
    Result findCategoryList(Long id);

    Result exportExcel(HttpServletResponse response);

    Result importExcel(MultipartFile file);
}
