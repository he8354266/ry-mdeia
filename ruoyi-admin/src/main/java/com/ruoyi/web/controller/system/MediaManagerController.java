package com.ruoyi.web.controller.system;

import com.ruoyi.system.domain.MediaManager;
import com.ruoyi.system.service.IMediaManagerService;
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
 * mediaController
 * 
 * @author ruoyi
 * @date 2024-05-28
 */
@RestController
@RequestMapping("/media/media")
public class MediaManagerController extends BaseController
{
    @Autowired
    private IMediaManagerService MediaManagerService;

    /**
     * 查询media列表
     */
    @GetMapping("/list")
    public TableDataInfo list(MediaManager ryMediaManager)
    {
        startPage();
        List<MediaManager> list = MediaManagerService.selectRyMediaManagerList(ryMediaManager);
        return getDataTable(list);
    }

    /**
     * 导出media列表
     */
    @Log(title = "media", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MediaManager ryMediaManager)
    {
        List<MediaManager> list = MediaManagerService.selectRyMediaManagerList(ryMediaManager);
        ExcelUtil<MediaManager> util = new ExcelUtil<MediaManager>(MediaManager.class);
        util.exportExcel(response, list, "media数据");
    }

    /**
     * 获取media详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(MediaManagerService.selectRyMediaManagerById(id));
    }

    /**
     * 新增media
     */
    @Log(title = "media", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MediaManager ryMediaManager)
    {
        return toAjax(MediaManagerService.insertRyMediaManager(ryMediaManager));
    }

    /**
     * 修改media
     */
    @Log(title = "media", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MediaManager ryMediaManager)
    {
        return toAjax(MediaManagerService.updateRyMediaManager(ryMediaManager));
    }

    /**
     * 删除media
     */
    @Log(title = "media", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(MediaManagerService.deleteRyMediaManagerByIds(ids));
    }
}
