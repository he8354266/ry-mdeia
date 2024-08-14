package com.ruoyi.system.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.system.domain.MediaManager;
import com.ruoyi.system.domain.PhoneLogin;
import com.ruoyi.system.mapper.MediaManagerMapper;
import com.ruoyi.system.mapper.PhoneLoginMapper;
import com.ruoyi.system.service.IPhoneLoginService;
import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 手机登录信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-08-14
 */
@Service
public class PhoneLoginServiceImpl extends ServiceImpl<PhoneLoginMapper, PhoneLogin> implements IPhoneLoginService {
    @Resource
    private PhoneLoginMapper phoneLoginMapper;



}
