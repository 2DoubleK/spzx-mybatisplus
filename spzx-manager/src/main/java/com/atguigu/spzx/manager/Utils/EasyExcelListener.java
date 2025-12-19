package com.atguigu.spzx.manager.Utils;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.atguigu.spzx.manager.mapper.CategoryMapper;
import com.atguigu.spzx.model.entity.product.Category;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

//不能交给spring作为组件管理
@Slf4j
public class EasyExcelListener<T> implements ReadListener<T> {
    //定义常量，每多少条数据库写一次数据库
    private static final int BATCH_COUNT = 100;
    //定义一个集合缓存数据
    private List<T> cacheList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
    //通过构造函数传递mapper，操作数据库
    private CategoryMapper categoryMapper;

    public EasyExcelListener(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    //从第二行开始将美韩读取内容都封装到T对象里
    @Override
    public void invoke(T t, AnalysisContext analysisContext) {
        //把每行数据的t放到cacheList里
        cacheList.add(t);
        if (cacheList.size() >= BATCH_COUNT) {
            //调用方法批量加到数据
            saveData();
            //清空缓存,直接new一个
            //cacheList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
            cacheList.clear();
        }
    }


    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        //保存数据,当数据不足100时也要保存时调用这个方法
        saveData();
        log.info("导入完成，共处理{}行数据", cacheList.size());
    }

    //保存方法
    private void saveData() {
        List<Category> categoryList = cacheList.stream().map(vo -> {
            Category category = new Category();
            BeanUtils.copyProperties(vo, category);
            return category;
        }).collect(Collectors.toList());
        categoryMapper.batchInsert(categoryList);
    }

}
