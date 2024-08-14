package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.system.domain.PwdManager;
import com.ruoyi.system.mapper.PwdManagerMapper;
import com.ruoyi.system.service.IPwdManagerService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * passwordService业务层处理
 * 
 * @author ruoyi
 * @date 2024-02-28
 */
@Service
public class PwdManagerServiceImpl extends
    ServiceImpl<PwdManagerMapper,PwdManager> implements IPwdManagerService
{
    @Resource
    private PwdManagerMapper pwdManagerMapper;

    /**
     * 查询password
     * 
     * @param id password主键
     * @return password
     */
    @Override
    public PwdManager selectPwdManagerById(Long id)
    {
        return pwdManagerMapper.selectPwdManagerById(id);
    }

    /**
     * 查询password列表
     * 
     * @param pwdManager password
     * @return password
     */
    @Override
    public List<PwdManager> selectPwdManagerList(PwdManager pwdManager)
    {
        return pwdManagerMapper.selectPwdManagerList(pwdManager);
    }

    /**
     * 新增password
     * 
     * @param pwdManager password
     * @return 结果
     */
    @Override
    public int insertPwdManager(PwdManager pwdManager)
    {
        return pwdManagerMapper.insertPwdManager(pwdManager);
    }

    /**
     * 修改password
     * 
     * @param pwdManager password
     * @return 结果
     */
    @Override
    public int updatePwdManager(PwdManager pwdManager)
    {
        return pwdManagerMapper.updatePwdManager(pwdManager);
    }

    /**
     * 批量删除password
     * 
     * @param ids 需要删除的password主键
     * @return 结果
     */
    @Override
    public int deletePwdManagerByIds(Long[] ids)
    {
        return pwdManagerMapper.deletePwdManagerByIds(ids);
    }

    /**
     * 删除password信息
     * 
     * @param id password主键
     * @return 结果
     */
    @Override
    public int deletePwdManagerById(Long id)
    {
        return pwdManagerMapper.deletePwdManagerById(id);
    }
}
