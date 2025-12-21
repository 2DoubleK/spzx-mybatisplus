package com.atguigu.spzx.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "分页结果VO")
public class PageVO<T> {
    @Schema(description = "当前页数据列表")
    private List<T> list;

    @Schema(description = "总条数")
    private Integer total;

    @Schema(description = "当前页")
    private Long current;

    @Schema(description = "每页条数")
    private Long size;

    // 构造方法
    public PageVO(List<T> list, Integer total, Long current, Long size) {
        this.list = list;
        this.total = total;
        this.current = current;
        this.size = size;
    }
}
