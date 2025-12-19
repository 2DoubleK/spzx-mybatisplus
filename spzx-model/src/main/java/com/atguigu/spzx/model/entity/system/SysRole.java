package com.atguigu.spzx.model.entity.system;

import com.atguigu.spzx.model.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "角色实体类")
@TableName("sys_role")
public class SysRole extends BaseEntity { //继承了BaseEntity所以可以省略一些字段比如id、createid create_time update_time is_deleted等

    private static final long serialVersionUID = 1L;

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "角色编码")
    private String roleCode;

    @Schema(description = "描述")
    private String description;


}
