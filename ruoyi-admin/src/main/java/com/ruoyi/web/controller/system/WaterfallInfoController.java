package com.ruoyi.web.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.MediaManager;
import com.ruoyi.system.domain.WaterfallInfo;
import com.ruoyi.system.service.IMediaManagerService;
import com.ruoyi.system.service.IWaterfallInfoService;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
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
 * waterfallController
 *
 * @author ruoyi
 * @date 2024-02-28
 */
@RestController
@RequestMapping("/waterfall/info")
public class WaterfallInfoController extends BaseController {

  @Autowired
  private IWaterfallInfoService waterfallInfoService;
  @Resource
  private IMediaManagerService mediaManagerService;

  /**
   * 查询waterfall列表
   */
  @GetMapping("/list")
  public TableDataInfo list(WaterfallInfo waterfallInfo) {
    QueryWrapper<WaterfallInfo> queryWrapper = Wrappers.query();
    if (ObjectUtils.isNotEmpty(waterfallInfo.getNumber())) {
      queryWrapper.like("number", waterfallInfo.getNumber());
    }
    if (ObjectUtils.isNotEmpty(waterfallInfo.getNickName())) {
      queryWrapper.like("nick_name", waterfallInfo.getNickName());
    }
    startPage();
    List<WaterfallInfo> list = waterfallInfoService.list(queryWrapper);
    return getDataTable(list);
  }

  /**
   * waterfall列表
   */
  @GetMapping("/waterfalllist")
  public TableDataInfo waterfalllist(WaterfallInfo waterfallInfo) {
    startPage();
    QueryWrapper<WaterfallInfo> queryWrapper = Wrappers.query(waterfallInfo);
    List<WaterfallInfo> list = waterfallInfoService.list(queryWrapper);
    if (CollectionUtils.isNotEmpty(list)) {
      for (WaterfallInfo waterfall : list) {
        Integer imgNum = mediaManagerService.lambdaQuery()
            .eq(MediaManager::getWaterfallId, waterfall.getId()).isNull(MediaManager::getVideo)
            .count();
        Integer videoNum = mediaManagerService.lambdaQuery()
            .eq(MediaManager::getWaterfallId, waterfall.getId()).isNull(MediaManager::getImg)
            .count();
        waterfall.setImgNum(imgNum);
        waterfall.setVideoNum(videoNum);
      }
    }
    return getDataTable(list);
  }


  /**
   * 获取waterfall详细信息
   */
  @GetMapping(value = "/{id}")
  public AjaxResult getInfo(@PathVariable("id") Long id) {
    WaterfallInfo waterfallInfo = waterfallInfoService.getById(id);
    if (ObjectUtils.isNotEmpty(waterfallInfo)) {
      List<MediaManager> imgList = mediaManagerService.lambdaQuery()
          .eq(MediaManager::getWaterfallId, waterfallInfo.getId()).isNull(MediaManager::getVideo)
          .list();
      List<MediaManager> videoList = mediaManagerService.lambdaQuery()
          .eq(MediaManager::getWaterfallId, waterfallInfo.getId()).isNull(MediaManager::getImg)
          .list();
      waterfallInfo.setImgList(imgList);
      waterfallInfo.setVideoList(videoList);
    }
    return success(waterfallInfo);
  }

  /**
   * 新增waterfall
   */
  @Log(title = "waterfall", businessType = BusinessType.INSERT)
  @PostMapping
  public AjaxResult add(@RequestBody WaterfallInfo waterfallInfo) {
    waterfallInfo.setCreateTime(new Date());
    waterfallInfo.setCreateBy(SecurityUtils.getUsername());
    if (CollectionUtils.isNotEmpty(waterfallInfo.getImgList())) {
      for (MediaManager media : waterfallInfo.getImgList()) {
        mediaManagerService.saveOrUpdate(media);
      }
    }
    if (CollectionUtils.isNotEmpty(waterfallInfo.getVideoList())) {
      for (MediaManager media : waterfallInfo.getVideoList()) {
        mediaManagerService.saveOrUpdate(media);
      }
    }
    return toAjax(waterfallInfoService.save(waterfallInfo));
  }

  /**
   * 修改waterfall
   */
  @Log(title = "waterfall", businessType = BusinessType.UPDATE)
  @PutMapping
  public AjaxResult edit(@RequestBody WaterfallInfo waterfallInfo) {
    waterfallInfo.setUpdateTime(new Date());
    waterfallInfo.setUpdateBy(SecurityUtils.getUsername());
    if (CollectionUtils.isNotEmpty(waterfallInfo.getImgList())) {
      for (MediaManager media : waterfallInfo.getImgList()) {
        mediaManagerService.saveOrUpdate(media);
      }
    }
    if (CollectionUtils.isNotEmpty(waterfallInfo.getVideoList())) {
      for (MediaManager media : waterfallInfo.getVideoList()) {
        mediaManagerService.saveOrUpdate(media);
      }
    }
    return toAjax(waterfallInfoService.saveOrUpdate(waterfallInfo));
  }

  /**
   * 删除waterfall
   */
  @Log(title = "waterfall", businessType = BusinessType.DELETE)
  @DeleteMapping("/{ids}")
  public AjaxResult remove(@PathVariable Long[] ids) {
    return toAjax(waterfallInfoService.removeIds(Arrays.asList(ids)));
  }
}
