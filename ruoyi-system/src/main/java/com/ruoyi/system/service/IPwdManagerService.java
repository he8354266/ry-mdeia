package com.ruoyi.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.domain.PwdManager;
import java.util.List;

/**
 * passwordService接口
 * 
 * @author ruoyi
 * @date 2024-02-28
 */
public interface IPwdManagerService extends IService<PwdManager> {


    /**
     * 查询password
     * 
     * @param id password主键
     * @return password
     */
    public PwdManager selectPwdManagerById(Long id);

    /**
     * 查询password列表
     * 
     * @param pwdManager password
     * @return password集合
     */
    public List<PwdManager> selectPwdManagerList(PwdManager pwdManager);

    /**
     * 新增password
     * 
     * @param pwdManager password
     * @return 结果
     */
    public int insertPwdManager(PwdManager pwdManager);

    /**
     * 修改password
     * 
     * @param pwdManager password
     * @return 结果
     */
    public int updatePwdManager(PwdManager pwdManager);

    /**
     * 批量删除password
     * 
     * @param ids 需要删除的password主键集合
     * @return 结果
     */
    public int deletePwdManagerByIds(Long[] ids);

    /**
     * 删除password信息
     * 
     * @param id password主键
     * @return 结果
     */
    public int deletePwdManagerById(Long id);
}
