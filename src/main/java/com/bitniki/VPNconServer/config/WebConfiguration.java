package com.bitniki.VPNconServer.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    //Allow request from any domain
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                //if you read this text you should know:
                //Author so dump, so he even forgets
                //to configure allowedOrigins properly
                .allowedOrigins("*")
                .allowedMethods("*");
    }
}
