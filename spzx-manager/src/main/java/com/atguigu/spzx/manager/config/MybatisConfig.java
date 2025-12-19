package com.atguigu.spzx.manager.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisConfig {
    @Bean
    public MybatisPlusInterceptor pageHelper() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 添加分页拦截器，指定数据库类型（MySQL/Oracle 等）
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        // 设置数据库类型（根据实际数据库调整）
        paginationInnerInterceptor.setDbType(DbType.MYSQL);
        // 开启溢出分页处理（默认值：false，溢出后返回空页；true 则返回最后一页）
        paginationInnerInterceptor.setOverflow(true);

        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        return interceptor;
    }
}
