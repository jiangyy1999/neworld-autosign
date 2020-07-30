package com.tbs.jyy.tool;

import com.alibaba.fastjson.JSON;
import com.tbs.jyy.entities.LogEntities;
import com.tbs.jyy.entities.NewWorldResponseBean;
import com.tbs.jyy.entities.UserEntities;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author tbs-jyy
 * @description 用来做自动签到脚本
 * @date 2020/7/29
 */
@Component
@ConfigurationProperties(prefix = "auto")
@PropertySource("classpath:autoConfig.properties")
public class AutoSignTool {
    private String loginUrl = "https://cn.neworld.date/auth/login";
    private String checkInUrl = "https://cn.neworld.date/user/checkin";
    private String checkUsernameFiled = "email";
    private String checkPasswdFiled = "passwd";

    @Autowired
    private PasswordEncoderTool encoderTool;

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public void setCheckInUrl(String checkInUrl) {
        this.checkInUrl = checkInUrl;
    }

    public List<LogEntities> checkInByList(List<UserEntities> list) {
        List<LogEntities> logList = new ArrayList<>();
        for (UserEntities userEntities : list) {
            userEntities.setPasswd(encoderTool.decode(userEntities.getPasswd()));
            logList.addAll(checkIn(userEntities));
        }
        return logList;
    }

    public List<LogEntities> checkInByUser(UserEntities user) {
        user.setPasswd(encoderTool.decode(user.getPasswd()));
        return checkIn(user);
    }

    /**
     * @param user : 解密后的user
     * @return 登录一条记录 , 签到一条记录
     **/
    private List<LogEntities> checkIn(UserEntities user) {
        HttpEntity loginEntity = null, checkEntity = null;
        CloseableHttpResponse loginResp = null, checkInResp = null;
        List<LogEntities> res = new ArrayList<>();  //保存log的列表
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(loginUrl);
            List<NameValuePair> fromList = new ArrayList<>();
            fromList.add(new BasicNameValuePair(checkUsernameFiled, user.getUsername()));
            fromList.add(new BasicNameValuePair(checkPasswdFiled, user.getPasswd()));
            httpPost.setEntity(new UrlEncodedFormEntity(fromList));

            //执行
            loginResp = client.execute(httpPost);
            loginEntity = loginResp.getEntity();
            NewWorldResponseBean jsonObject
                    = JSON.parseObject(EntityUtils.toString(loginEntity), NewWorldResponseBean.class);
            if (jsonObject.isSuccess()) {   //登录成功
                httpPost = new HttpPost(checkInUrl);
                checkInResp = client.execute(httpPost);
                checkEntity = checkInResp.getEntity();
                NewWorldResponseBean newWorldResponseBean
                        = JSON.parseObject(EntityUtils.toString(checkEntity), NewWorldResponseBean.class);
                res.add(new LogEntities().setType(LogEntities.CHECKIN_TYPE)
                        .setSuccess(newWorldResponseBean.isSuccess())
                        .setMsg(newWorldResponseBean.getMsg())
                        .setUserId(user.getId()));
            }
            res.add(new LogEntities().setType(LogEntities.LOGIN_TYPE)
                    .setSuccess(jsonObject.isSuccess()).setMsg(jsonObject.getMsg()).setUserId(user.getId()));
        } catch (IOException e) {
            res.add(new LogEntities().setType(LogEntities.SYSTEM_TYPE).setSuccess(false)
                    .setMsg("系统发生了故障 >>> " + e.getMessage()).setUserId(user.getId()));
            e.printStackTrace();
        } finally {
            try {
                EntityUtils.consume(loginEntity);
                EntityUtils.consume(checkEntity);
                if (loginResp != null) loginResp.close();
                if (checkInResp != null) checkInResp.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    @Override
    public String toString() {
        return "AutoSignTool{" +
                "loginUrl='" + loginUrl + '\'' +
                ", checkInUrl='" + checkInUrl + '\'' +
                '}';
    }
}
