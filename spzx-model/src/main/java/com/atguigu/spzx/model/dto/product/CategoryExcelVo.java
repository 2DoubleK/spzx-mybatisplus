package com.atguigu.spzx.model.dto.product;


import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@HeadRowHeight(20)
@ContentRowHeight(15)
public class CategoryExcelVo {

    @ExcelProperty(value = "分类ID", index = 0)  // Excel列名 + 列索引
    @ColumnWidth(8)  // 列宽
    private Long id;

    @ExcelProperty(value = "分类名称", index = 1)
    @ColumnWidth(15)
    private String name;

    @ExcelProperty(value = "图片URL", index = 2)
    @ColumnWidth(40)
    private String imageUrl;

    @ExcelProperty(value = "上级分类ID", index = 3)
    @ColumnWidth(10)
    private Long parentId;

    @ExcelProperty(value = "状态", index = 4)
    @ColumnWidth(8)
    private Integer status;

    @ExcelProperty(value = "序号", index = 5)
    @ColumnWidth(8)
    private Integer orderNum;

    @ExcelProperty(value = "创建时间", index = 6)
    @ColumnWidth(20)
    private Date createTime;


}
