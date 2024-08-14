package com.ruoyi.framework.web.domain;

import lombok.Data;

/**
 * 登录短信
 *
 * @author ruoyi
 */
@Data
public class LoginSms {

  /**
   * 手机号
   */
  private String phoneNumber;

  /**
   * 短信验证码
   */
  private String smsCode;



}
