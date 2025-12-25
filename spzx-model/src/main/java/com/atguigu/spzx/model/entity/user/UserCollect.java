package com.atguigu.spzx.model.entity.user;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户收藏表实体类
 * 对应数据库表：user_collect
 */
@Data
@TableName("user_collect")
public class UserCollect implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO) // 主键自增，与数据库AUTO_INCREMENT匹配
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 商品skuID
     */
    private Long skuId;

    /**
     * 创建时间
     * 数据库默认值：CURRENT_TIMESTAMP，MyBatis-Plus插入时自动填充（也可依赖数据库默认）
     */
    @TableField(fill = FieldFill.INSERT) // 插入时自动填充（需配置填充处理器）
    private LocalDateTime createTime;

    /**
     * 更新时间
     * 数据库默认值：CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
     */
    @TableField(fill = FieldFill.INSERT_UPDATE) // 插入/更新时自动填充
    private LocalDateTime updateTime;

    /**
     * 逻辑删除标识（0-未删除，1-已删除）
     * MyBatis-Plus逻辑删除注解，与数据库默认值0匹配
     */
    @TableLogic
    private Integer isDeleted;
}
