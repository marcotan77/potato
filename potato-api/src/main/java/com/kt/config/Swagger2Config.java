package com.kt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @program: potato
 * @description swagger配置
 * @Author: Tan.
 * @Date: 2020-12-03 17:27
 **/
@Configuration
@EnableSwagger2
public class Swagger2Config {

    // 访问地址 http://localhost:8088/swagger-ui.html 原路径
    // 访问地址 http://localhost:8088/doc.html
    // 配置swagger2 核心配置 docket
    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2) // 指定api类型为swagger2
                .apiInfo(apiInfo())                           // 用于定义api文档汇总信息
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.kt.controller")) //指定controller包
                .paths(PathSelectors.any()) //所有controller
                .build();

    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("土豆api") // 文档页标题
                .contact(new Contact("potato",
                        "http://www.baidu.com",
                        "746261725@qq.com")) // 联系人信息
                .description("api文档") // 详细信息
                .version("1.0.1") // 文档版本号
                .termsOfServiceUrl("http://www.baidu.com") // 网站地址
                .build();
    }

}
