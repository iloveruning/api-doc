package com.github.llchen.apidoc.config;


import com.github.llchen.apidoc.core.ApiDocContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author llchen12
 * @date 2018/6/11
 */
@Configuration
@ComponentScan(basePackages = "com.github.llchen.apidoc.controller")
@EnableConfigurationProperties(ApiDocProperties.class)
public class ApiDocAutoConfiguration {


    private ApiDocProperties properties;

    public ApiDocAutoConfiguration(ApiDocProperties properties) {
        this.properties = properties;
    }


    @Bean
    @ConditionalOnMissingBean(ThreadPoolTaskExecutor.class)
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(5);
        taskExecutor.setMaxPoolSize(10);
        taskExecutor.setKeepAliveSeconds(60);
        return taskExecutor;
    }

    @Bean
    public ApiDocContext apiDocContext() {
        return new ApiDocContext(properties);
    }
}
