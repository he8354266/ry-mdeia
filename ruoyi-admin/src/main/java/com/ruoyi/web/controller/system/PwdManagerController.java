package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.system.domain.PwdManager;
import com.ruoyi.system.mapper.WaterfallInfoMapper;
import com.ruoyi.system.service.IPwdManagerService;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
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
 * passwordController
 *
 * @author ruoyi
 * @date 2024-02-28
 */
@Anonymous
@RestController
@RequestMapping("/waterfall/manager")
public class PwdManagerController extends BaseController {

  @Autowired
  private IPwdManagerService pwdManagerService;


  /**
   * 密码校验
   */
  @Anonymous
  @PostMapping("/pwdCheck")
  public AjaxResult pwdCheck(@RequestBody PwdManager pwdManager) {
    if (ObjectUtils.isEmpty(pwdManager.getPassword())) {
      throw new RuntimeException("密码不能为空");
    }
    List<PwdManager> managerList = pwdManagerService.lambdaQuery()
        .eq(PwdManager::getPassword, pwdManager.getPassword()).list();
    if (CollectionUtils.isEmpty(managerList)) {
      throw new RuntimeException("密码错误");
    }
    return AjaxResult.success();
  }

  /**
   * 查询password列表
   */
  @GetMapping("/list")
  public TableDataInfo list(PwdManager pwdManager) {
    startPage();
    List<PwdManager> list = pwdManagerService.selectPwdManagerList(pwdManager);
    return getDataTable(list);
  }

  /**
   * 导出password列表
   */
  @Log(title = "password", businessType = BusinessType.EXPORT)
  @PostMapping("/export")
  public void export(HttpServletResponse response, PwdManager pwdManager) {
    List<PwdManager> list = pwdManagerService.selectPwdManagerList(pwdManager);
    ExcelUtil<PwdManager> util = new ExcelUtil<PwdManager>(PwdManager.class);
    util.exportExcel(response, list, "password数据");
  }

  /**
   * 获取password详细信息
   */
  @GetMapping(value = "/{id}")
  public AjaxResult getInfo(@PathVariable("id") Long id) {
    return success(pwdManagerService.selectPwdManagerById(id));
  }

  /**
   * 新增password
   */
  @Log(title = "password", businessType = BusinessType.INSERT)
  @PostMapping
  public AjaxResult add(@RequestBody PwdManager pwdManager) {
    return toAjax(pwdManagerService.insertPwdManager(pwdManager));
  }

  /**
   * 修改password
   */
  @Log(title = "password", businessType = BusinessType.UPDATE)
  @PutMapping
  public AjaxResult edit(@RequestBody PwdManager pwdManager) {
    return toAjax(pwdManagerService.updatePwdManager(pwdManager));
  }

  /**
   * 删除password
   */
  @Log(title = "password", businessType = BusinessType.DELETE)
  @DeleteMapping("/{ids}")
  public AjaxResult remove(@PathVariable Long[] ids) {
    return toAjax(pwdManagerService.deletePwdManagerByIds(ids));
  }
}
