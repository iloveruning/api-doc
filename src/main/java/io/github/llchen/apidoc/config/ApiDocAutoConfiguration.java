package io.github.llchen.apidoc.config;


import io.github.llchen.apidoc.core.ApiDocContext;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author llchen12
 * @date 2018/6/11
 */
@Configuration
@ComponentScan(basePackages = "io.github.llchen.apidoc.controller")
@EnableConfigurationProperties(ApiDocProperties.class)
public class ApiDocAutoConfiguration {


    private ApiDocProperties properties;

    public ApiDocAutoConfiguration(ApiDocProperties properties) {
        this.properties = properties;
    }


//    @Bean
//    public ThreadPoolTaskExecutor taskExecutor() {
//        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
//        taskExecutor.setCorePoolSize(5);
//        taskExecutor.setMaxPoolSize(10);
//        taskExecutor.setKeepAliveSeconds(60);
//        return taskExecutor;
//    }

    @Bean
    public ApiDocContext apiDocContext() {
        return new ApiDocContext(properties);
    }
}
