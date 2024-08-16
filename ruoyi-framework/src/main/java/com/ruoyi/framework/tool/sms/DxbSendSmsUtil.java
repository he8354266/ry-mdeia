package com.ruoyi.framework.tool.sms;

import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;


public class DxbSendSmsUtil {

  private static final String URL = "http://api.smsbao.com/sms"; //国内请求路径
  private static final String USERNAME = "he8354266";
  private static final String PASSWORD = "heyang94216";

  public static String SMS(String number, String mobileCode) {
    String content = new String(
        "【沃顿科技】您的验证码是" + mobileCode + "。如非本人操作，请忽略本短信。");

    String url = URL.concat("/?u=").concat(USERNAME).concat("&p=").concat(PASSWORD).concat("&g=")
        .concat("&m=").concat(number).concat("&c=").concat(content);

    OkHttpClient httpClient = new OkHttpClient();

    Request request = new Request.Builder()
        .url(url)
        .get()
        .build();

    try {
      Response response = httpClient.newCall(request).execute();
      if (response.isSuccessful()) {
        String responseData = response.body().string();
        System.out.println(responseData);
        return responseData;
      } else {
        System.out.println("GET request failed. Response code: " + response.code());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return "";
  }
}