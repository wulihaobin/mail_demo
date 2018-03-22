package com.nf.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class MailServletImpl implements MailService {

    @Autowired
    private MailSender mailSender;

    @Override
    public String send(String to, String what) {
        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom("957771831@qq.com");
        message.setSubject("主题：我的邮箱");
        message.setText(what);
        message.setTo(to);
        mailSender.send(message);
        return "success";
    }

    @Override
    public String send(String to, String what,String type) {
        return null;
    }
}
