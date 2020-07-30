package com.tbs.jyy.config;

import com.tbs.jyy.service.UserServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author tbs-jyy
 * @description 定时任务
 * @date 2020/7/30
 */
@EnableScheduling
@Configuration
public class AutoCheckInConfiguration {
    @Autowired
    private UserServiceI userService;

    @Scheduled(cron = "0 0 8 * * ?")
    private void test() {
        userService.checkInAll();
    }
}
