package com.nf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
@Component
public class TestController {

    @Autowired
    private Sender sender;

    @Scheduled(fixedRate = 3000)
    public void test () {
        sender.send("1023712507@qq.com", "你吃了屎吗");
        System.out.println("发送成功");
    }
}
