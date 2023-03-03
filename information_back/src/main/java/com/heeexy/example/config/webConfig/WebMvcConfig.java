package com.heeexy.example.config.webConfig;

import com.heeexy.example.util.old.UploadProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Resource
    private UploadProperties upload;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("file:"+upload.getRoot());
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }
}
