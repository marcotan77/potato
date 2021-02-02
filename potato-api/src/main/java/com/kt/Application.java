package com.kt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @program: potato
 * @description 启动类
 * @author: Tan.
 * @create: 2020-11-11 17:09
 **/
@SpringBootApplication
// 扫描mybatis 通用mapper
@MapperScan(basePackages = "com.kt.mapper")
@ComponentScan(basePackages = {"com.kt","org.n3r.idworker"})
//@EnableScheduling // 开启定时任务
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}
