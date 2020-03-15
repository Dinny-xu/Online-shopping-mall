package com.changgou.Item.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author ：炎炎
 * @date ：Created in 2019/10/14 11:07
 * @description：Java 代码方式配置
 */


@Configuration
public class RequestHandler  implements WebMvcConfigurer{

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/item/**")
                .addResourceLocations("classpath:/templates/item/");
    }
}
