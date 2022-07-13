package com.sias.regiee.config;

import com.sias.regiee.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.cbor.MappingJackson2CborHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

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


    /*2.扩展MVC框架的消息转换器
    *   把自己需要的加载MVC框架中*/
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        /*01.创建消息转换器*/
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        /*02.设置消息转换器，底层使用的是Jackson将Java对象转为json*/
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        /*03.将上面设置的消息转换器加在MVC框架的转换器集合中
        *    加在前面，可以提前使用自己的*/
        converters.add(0,messageConverter);
    }
}
