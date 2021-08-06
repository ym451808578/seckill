package com.example.seckill.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Castle
 * @Date 2021/8/4 3:20 下午
 */
@Configuration
public class FilterRegistration {
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        //Filter可以new，也可以使用依赖注入Bean
        registration.setFilter(new AuthenticationFilter());
        //过滤器名称
        registration.setName("authenticationFilter");
        //拦截路径
        registration.addUrlPatterns("/goods/*");
        //设置顺序
        registration.setOrder(0);
        return registration;
    }
}
