package com.ruoyi.framework.web.service;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.ruoyi.framework.tool.sms.SlSendSmsUtil;
import com.ruoyi.framework.web.domain.LoginSms;

import com.ruoyi.framework.tool.sms.SendSmsUtil;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.exception.user.BlackListException;
import com.ruoyi.common.exception.user.CaptchaException;
import com.ruoyi.common.exception.user.CaptchaExpireException;
import com.ruoyi.common.exception.user.UserNotExistsException;
import com.ruoyi.common.exception.user.UserPasswordNotMatchException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.framework.security.context.AuthenticationContextHolder;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysUserService;

/**
 * 登录校验方法
 *
 * @author ruoyi
 */
@Component
public class SysLoginService {

  @Autowired
  private TokenService tokenService;

  @Resource
  private AuthenticationManager authenticationManager;

  @Autowired
  private RedisCache redisCache;

  @Autowired
  private ISysUserService userService;

  @Autowired
  private ISysConfigService configService;

  private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");

  /**
   * 手机号登录
   *
   * @param loginSms 用户名
   */
  public boolean loginBySms(LoginSms loginSms) {
    if (ObjectUtils.isEmpty(loginSms.getPhoneNumber())) {
      throw new ServiceException("手机号不能为空");
    }
    if(ObjectUtils.isEmpty(loginSms.getRobotState())) {
     throw new ServiceException("机器人验证状态不能为空");
    }
    if (loginSms.getRobotState() == 0) {
      throw new ServiceException("机器人验证未通过");
    }
    if (ObjectUtils.isEmpty(loginSms.getSmsCode())) {
      throw new ServiceException("验证码不能为空");
    }
    boolean isValid = isValidPhoneNumber(loginSms.getPhoneNumber());
    if (!isValid) {
      throw new ServiceException("手机号格式错误");
    }
    Integer smsCode = redisCache.getCacheObject("smsCode:" + loginSms.getPhoneNumber());
    if (ObjectUtils.isNotEmpty(smsCode)) {
      if (!String.valueOf(smsCode).equals(loginSms.getSmsCode())) {
        throw new ServiceException("验证码错误");
      } else {
        redisCache.setCacheObject("smsCache", loginSms.getPhoneNumber(), 1,
            TimeUnit.HOURS);
        return true;
      }
    } else {
      throw new ServiceException("验证码已失效，请重新获取");
    }
  }

  /**
   * 获取登录状态
   *
   * @param loginSms 用户名
   */
  public boolean getLoginState(LoginSms loginSms) throws IOException {
    if (ObjectUtils.isEmpty(loginSms.getPhoneNumber())) {
      throw new ServiceException("手机号不能为空");
    }
    boolean isValid = isValidPhoneNumber(loginSms.getPhoneNumber());
    if (!isValid) {
      throw new ServiceException("手机号格式错误");
    }
    String smsCache = redisCache.getCacheObject("smsCache");
    if (ObjectUtils.isNotEmpty(smsCache)) {
      return true;
    } else {
      return false;
    }
  }


  /**
   * 验证码发送验证
   *
   * @param loginSms 用户名
   */
  public void sendSms(LoginSms loginSms) throws IOException {
    if (ObjectUtils.isEmpty(loginSms.getPhoneNumber())) {
      throw new ServiceException("手机号不能为空");
    }
    if(ObjectUtils.isEmpty(loginSms.getRobotState())) {
      throw new ServiceException("机器人验证状态不能为空");
    }
    if (loginSms.getRobotState() == 0) {
      throw new ServiceException("机器人验证未通过");
    }
    boolean isValid = isValidPhoneNumber(loginSms.getPhoneNumber());
    if (!isValid) {
      throw new ServiceException("手机号格式错误");
    }
    Integer smsCode = redisCache.getCacheObject("smsCode:" + loginSms.getPhoneNumber());
    if (ObjectUtils.isNotEmpty(smsCode)) {
      throw new ServiceException("请勿频繁发送验证码，请稍后再试");
    }
    //发送验证码
    //随机编号
    int mobileCode = (int) ((Math.random() * 9 + 1) * 100000);
    String smsState = SendSmsUtil.SMS(loginSms.getPhoneNumber(), String.valueOf(mobileCode));
    if (smsState.equals("2")) {
      redisCache.setCacheObject("smsCode:" + loginSms.getPhoneNumber(), mobileCode, 190000,
          TimeUnit.MILLISECONDS);
    } else {
      throw new ServiceException("发送失败");
    }
  }


  public static boolean isValidPhoneNumber(String phoneNumber) {
    // 检查phoneNumber是否在正则表达式的匹配范围内
    return PHONE_NUMBER_PATTERN.matcher(phoneNumber).matches();
  }

  /**
   * 登录验证
   *
   * @param username 用户名
   * @param password 密码
   * @param code     验证码
   * @param uuid     唯一标识
   * @return 结果
   */
  public String login(String username, String password, String code, String uuid) {
    // 验证码校验
    validateCaptcha(username, code, uuid);
    // 登录前置校验
    loginPreCheck(username, password);
    // 用户验证
    Authentication authentication = null;
    try {
      UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
          username, password);
      AuthenticationContextHolder.setContext(authenticationToken);
      // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
      authentication = authenticationManager.authenticate(authenticationToken);
    } catch (Exception e) {
      if (e instanceof BadCredentialsException) {
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL,
            MessageUtils.message("user.password.not.match")));
        throw new UserPasswordNotMatchException();
      } else {
        AsyncManager.me()
            .execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
        throw new ServiceException(e.getMessage());
      }
    } finally {
      AuthenticationContextHolder.clearContext();
    }
    AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS,
        MessageUtils.message("user.login.success")));
    LoginUser loginUser = (LoginUser) authentication.getPrincipal();
    recordLoginInfo(loginUser.getUserId());
    // 生成token
    return tokenService.createToken(loginUser);
  }

  /**
   * 校验验证码
   *
   * @param username 用户名
   * @param code     验证码
   * @param uuid     唯一标识
   * @return 结果
   */
  public void validateCaptcha(String username, String code, String uuid) {
    boolean captchaEnabled = configService.selectCaptchaEnabled();
    if (captchaEnabled) {
      String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
      String captcha = redisCache.getCacheObject(verifyKey);
      redisCache.deleteObject(verifyKey);
      if (captcha == null) {
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL,
            MessageUtils.message("user.jcaptcha.expire")));
        throw new CaptchaExpireException();
      }
      if (!code.equalsIgnoreCase(captcha)) {
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL,
            MessageUtils.message("user.jcaptcha.error")));
        throw new CaptchaException();
      }
    }
  }

  /**
   * 登录前置校验
   *
   * @param username 用户名
   * @param password 用户密码
   */
  public void loginPreCheck(String username, String password) {
    // 用户名或密码为空 错误
    if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
      AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL,
          MessageUtils.message("not.null")));
      throw new UserNotExistsException();
    }
    // 密码如果不在指定范围内 错误
    if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
        || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
      AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL,
          MessageUtils.message("user.password.not.match")));
      throw new UserPasswordNotMatchException();
    }
    // 用户名不在指定范围内 错误
    if (username.length() < UserConstants.USERNAME_MIN_LENGTH
        || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
      AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL,
          MessageUtils.message("user.password.not.match")));
      throw new UserPasswordNotMatchException();
    }
    // IP黑名单校验
    String blackStr = configService.selectConfigByKey("sys.login.blackIPList");
    if (IpUtils.isMatchedIp(blackStr, IpUtils.getIpAddr())) {
      AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL,
          MessageUtils.message("login.blocked")));
      throw new BlackListException();
    }
  }

  /**
   * 记录登录信息
   *
   * @param userId 用户ID
   */
  public void recordLoginInfo(Long userId) {
    SysUser sysUser = new SysUser();
    sysUser.setUserId(userId);
    sysUser.setLoginIp(IpUtils.getIpAddr());
    sysUser.setLoginDate(DateUtils.getNowDate());
    userService.updateUserProfile(sysUser);
  }
}
