package com.ruoyi.framework.tool.sms;

import java.io.IOException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;


public class SlSendSmsUtil {

  private static final String URL = "http://sms.106jiekou.com/utf8/sms.aspx"; //国内请求路径
  private static final String APPID = "he8354266";
  private static final String APIKEY = "heyang94216";

  public static String SMS(String number, String mobileCode) throws HttpException, IOException {

    HttpClient client = new HttpClient();
    PostMethod method = new PostMethod(URL);

    client.getParams().setContentCharset("utf-8");
    method.setRequestHeader("ContentType", "application/x-www-form-urlencoded;charset=utf-8");

    String content = new String("您的订单编码：" + mobileCode + "。如需帮助请联系客服。");

    NameValuePair[] data = {//提交短信
        new NameValuePair("account", APPID), //查看用户名 登录用户中心->验证码通知短信>产品总览->API接口信息->APIID
        new NameValuePair("password", APIKEY), //查看密码 登录用户中心->验证码通知短信>产品总览->API接口信息->APIKEY
        new NameValuePair("mobile", number),
        new NameValuePair("content", content),
    };
    method.setRequestBody(data);

    try {
      client.executeMethod(method);

      String code = method.getResponseBodyAsString();
      if ("100".equals(code)) {
        System.out.println("短信提交成功");
        System.out.println(mobileCode);
      }
      return code;
    } catch (HttpException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return "";
  }
}