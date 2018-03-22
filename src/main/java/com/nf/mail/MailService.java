package com.nf.mail;

import com.nf.Sender;

public interface MailService extends Sender {
    String send(String to,String what,String type);
}
