package com.atguigu.spzx.model.entity.order;

import com.atguigu.spzx.model.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("order_statistics") // 指定数据库表名
public class OrderStatistics extends BaseEntity {

    // 补充数据库表中存在的字段：省份编码（原实体类缺失）
    @TableField("province_code")
    private String provinceCode;
    @TableField("order_date")
    private Date orderDate;
    private BigDecimal totalAmount;
    private Integer totalNum;

    // 若有其他非数据库字段，添加@TableField(exist = false)
    // 示例：
    // @Schema(description = "省份名称")
    // @TableField(exist = false)
    // private String provinceName;

}