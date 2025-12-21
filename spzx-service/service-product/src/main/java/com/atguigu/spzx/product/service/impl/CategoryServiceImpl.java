package com.atguigu.spzx.product.service.impl;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.atguigu.spzx.model.entity.product.Category;
import com.atguigu.spzx.product.mapper.CategoryMapper;
import com.atguigu.spzx.product.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.atguigu.spzx.product.utils.Constants.CAT_ONE_KEY;
import static com.atguigu.spzx.product.utils.Constants.EXPIRE_TIME;


@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public List<Category> findOneCategory() {
        //先从redis中查询一级分类
        String cat_one_json = redisTemplate.opsForValue().get(CAT_ONE_KEY);
        //有，返回缓存
        if (StringUtils.isNotBlank(cat_one_json)) {
            return JSON.parseArray(cat_one_json, Category.class);
        }
        //没有在查数据库，将查到的结果放到redis
        List<Category> categories = categoryMapper.findAllTopCate();
        try {
            if (categories != null) {
                //放入redis
                redisTemplate.opsForValue().set(CAT_ONE_KEY, JSON.toJSONString(categories), EXPIRE_TIME, TimeUnit.MINUTES);
            }
        } catch (Exception e) {
            log.error("写入redis失败{}",e);
        }
        return categories;
    }

    //查询所有分类按照树形结构封装
    @Cacheable(value = "category",key = "'all'")   //放入redis的结果就是category::all
    @Override
    public List<Category> findCategoryTree() {
        //1.查询所有分类
        List<Category> categories = lambdaQuery().eq(Category::getIsDeleted, 0).list();
        // 空指针防护：如果分类列表为null，返回空列表
        if (CollectionUtils.isEmpty(categories)) {
            return new ArrayList<>();
        }
        //2.对所有分类进行分级递归，封装到集合里然后返回
        //遍历一级目录，为每一个一级目录添加二级目录
        List<Category> root = categories.stream().filter(item -> item.getParentId() == 0).collect(Collectors.toList());
        root.forEach(category1 -> {
            List<Category> second = categories.stream()
                    .filter(one -> one.getParentId() == category1.getId()).collect(Collectors.toList());
            category1.setChildren(second);
            //遍历二级目录
            second.forEach(category2 -> {
                List<Category> third = categories.stream()
                        .filter(two -> two.getParentId() == category2.getId()).collect(Collectors.toList());
                category2.setChildren(third);
            });
        });
        return root;
    }
}
