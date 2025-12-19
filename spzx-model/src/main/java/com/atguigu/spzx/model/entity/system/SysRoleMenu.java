package com.atguigu.spzx.model.entity.system;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

@Data
@TableName("sys_role_menu") // 绑定数据库表名
public class SysRoleMenu {
    private Long id;               // 对应表中id字段
    private Long roleId;           // 对应表中role_id字段（MyBatis-Plus默认驼峰映射）
    private Long menuId;           // 对应表中menu_id字段
    private Date createTime;       // 对应表中create_time字段
    private Date updateTime;       // 对应表中update_time字段
    private Integer isDeleted;     // 对应表中is_deleted字段
    private Integer isHalf;        // 对应表中is_half字段
}