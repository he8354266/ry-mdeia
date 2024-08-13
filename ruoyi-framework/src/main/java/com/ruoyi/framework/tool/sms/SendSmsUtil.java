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


public class SendSmsUtil {

  private static final String URL = "http://106.ihuyi.com/webservice/sms.php?method=Submit"; //国内请求路径
  private static final String APPID = "C51419892";
  private static final String APIKEY = "efea37ed32d41480f0df53c6b24ae04f";

  public static String SMS(String number, String mobileCode) throws HttpException, IOException {

    HttpClient client = new HttpClient();
    PostMethod method = new PostMethod(URL);

    client.getParams().setContentCharset("utf-8");
    method.setRequestHeader("ContentType", "application/x-www-form-urlencoded;charset=utf-8");

    String content = new String("您的验证码是：" + mobileCode + "。请不要把验证码泄露给其他人。");

    NameValuePair[] data = {//提交短信
        new NameValuePair("account", APPID), //查看用户名 登录用户中心->验证码通知短信>产品总览->API接口信息->APIID
        new NameValuePair("password", APIKEY), //查看密码 登录用户中心->验证码通知短信>产品总览->API接口信息->APIKEY
        new NameValuePair("mobile", number),
        new NameValuePair("content", content),
    };
    method.setRequestBody(data);

    try {
      client.executeMethod(method);

      String SubmitResult = method.getResponseBodyAsString();

      Document doc = DocumentHelper.parseText(SubmitResult);
      Element root = doc.getRootElement();

      String code = root.elementText("code");
      String msg = root.elementText("msg");
      String smsid = root.elementText("smsid");

      System.out.println("code" + code);
      System.out.println("msg" + msg);
      System.out.println("smsid" + smsid);
      System.out.println("mo" + mobileCode);
      if ("2".equals(code)) {
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
    } catch (DocumentException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return "";
  }
}