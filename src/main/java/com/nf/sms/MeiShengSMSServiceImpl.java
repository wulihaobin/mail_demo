package com.nf.sms;

import com.nf.sms.utils.Commons;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

public class MeiShengSMSServiceImpl implements SMSService {
    // 将参数写在配置文件中
    @Value("${sms.meisheng.url}")
    String httpUrl;
    @Value("${sms.meisheng.sid}")
    String accountSid;
    @Value("${sms.meisheng.apikey}")
    String accountApikey;
    @Value("${sms.meisheng.extno}")
    String extno;
    @Value("${sms.meisheng.encode}")
    String encoding = "utf-8";

    @Override
    public String send(String to, String what, int templateNo) {
        String resultJson = "";
        CloseableHttpClient httpClient = null;
        try {
            // 第一步、构造发送的内容
            HttpPost sms = new HttpPost(httpUrl + "/sms/sendtplsms.json");
            sms.setHeader("Content-Type", "application/x-www-form-urlencoded");
            sms.setHeader("Content-Encoding", encoding);
            List<NameValuePair> nvps = Arrays.asList(
                    new BasicNameValuePair("sid", accountSid),
                    new BasicNameValuePair("sign", encrypt(accountSid + accountApikey + templateNo + to + what)),
                    new BasicNameValuePair("extno", extno),
                    new BasicNameValuePair("mobile", to),
                    new BasicNameValuePair("content", what),
                    new BasicNameValuePair("tplid", templateNo + ""));
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nvps, encoding);
            sms.setEntity(entity);

            // 第二步、通过 httpclient 发送信息出去
            httpClient = HttpClientBuilder.create().build();
            HttpResponse response = httpClient.execute(sms);

            // 第三步、得到并处理返回结果
            HttpEntity httpEntity = response.getEntity();
            resultJson = EntityUtils.toString(httpEntity, encoding);

            EntityUtils.consume(entity);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return resultJson;
    }

    @Override
    public String send(String to, String what) {
        return send(to, what, 9);
    }

    private String encrypt(String msg) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return Commons.md5Digest(Commons.changeCharset(msg, encoding), encoding);
    }
}
