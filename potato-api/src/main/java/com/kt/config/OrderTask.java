package com.kt.config;

import com.kt.service.OrderService;
import com.kt.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @program: potato
 * @description
 * @Author: Tcs
 * @Date: 2020-12-16 10:01
 **/
@Component
public class OrderTask {

    @Autowired
    private OrderService orderService;

    @Scheduled(cron = "0/3 * * * * ?")
    public void autoCloseOrder(){
        orderService.closeOrder();
        System.out.println("执行定时任务，当前时间："+ DateUtil.getCurrentDateString(DateUtil.DATETIME_PATTERN));

    }
}
