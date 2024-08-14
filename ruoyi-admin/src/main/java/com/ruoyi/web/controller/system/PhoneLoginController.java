package com.ruoyi.web.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.system.domain.PhoneLogin;
import com.ruoyi.system.service.IPhoneLoginService;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 手机登录信息Controller
 * 
 * @author ruoyi
 * @date 2024-08-14
 */
@RestController
@RequestMapping("/login/login")
public class PhoneLoginController extends BaseController
{
    @Autowired
    private IPhoneLoginService phoneLoginService;

    /**
     * 查询手机登录信息列表
     */
    @GetMapping("/list")
    public TableDataInfo list(PhoneLogin phoneLogin)
    {
        startPage();
        QueryWrapper<PhoneLogin> queryWrapper = Wrappers.query(phoneLogin);
        List<PhoneLogin> list = phoneLoginService.list(queryWrapper);
        return getDataTable(list);
    }



    /**
     * 获取手机登录信息详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(phoneLoginService.getById(id));
    }

    /**
     * 新增手机登录信息
     */
    @Log(title = "手机登录信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PhoneLogin phoneLogin)
    {
        return toAjax(phoneLoginService.save(phoneLogin));
    }

    /**
     * 修改手机登录信息
     */
    @Log(title = "手机登录信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PhoneLogin phoneLogin)
    {
        return toAjax(phoneLoginService.saveOrUpdate(phoneLogin));
    }

    /**
     * 删除手机登录信息
     */
    @PreAuthorize("@ss.hasPermi('login:login:remove')")
    @Log(title = "手机登录信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(phoneLoginService.removeByIds(Arrays.asList(ids)));
    }
}
