package com.sias.regiee.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author Edgar
 * @create 2022-05-27 14:52
 * @faction:*/


@Configuration
@Slf4j
public class WebMvcConfig extends WebMvcConfigurationSupport {


     /*1.添加放行的路径
     *   目的是为了访问静态资源，这个路径
     *   是自己定义的*/
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("执行成功");
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
    }
}
