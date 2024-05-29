package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.system.domain.MediaManager;
import com.ruoyi.system.domain.PwdManager;
import com.ruoyi.system.domain.WaterfallInfo;
import com.ruoyi.system.mapper.PwdManagerMapper;
import com.ruoyi.system.mapper.WaterfallInfoMapper;
import com.ruoyi.system.service.IMediaManagerService;
import com.ruoyi.system.service.IPwdManagerService;
import com.ruoyi.system.service.IWaterfallInfoService;
import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * waterfallService业务层处理
 *
 * @author ruoyi
 * @date 2024-02-28
 */
@Service
public class WaterfallInfoServiceImpl extends
    ServiceImpl<WaterfallInfoMapper, WaterfallInfo> implements IWaterfallInfoService {

  @Resource
  private WaterfallInfoMapper waterfallInfoMapper;
  @Resource
  private IWaterfallInfoService waterfallInfoService;
  @Resource
  private IMediaManagerService mediaManagerService;



  @Override
  @Transactional(rollbackFor = Exception.class)
  public int removeIds(List<Long> list) {
    FileUtils fileUtils = new FileUtils();
    waterfallInfoService.removeByIds(list);
    for (Long aLong : list) {
      //删除多媒体
      List<MediaManager> managerList = mediaManagerService.lambdaQuery()
          .eq(MediaManager::getWaterfallId, aLong).list();
      if (CollectionUtils.isNotEmpty(managerList)) {
        for (MediaManager mediaManager : managerList) {
          mediaManagerService.removeById(mediaManager.getId());
          //删除文件
          if (ObjectUtils.isNotEmpty(mediaManager.getImg()) && ObjectUtils.isEmpty(
              mediaManager.getVideo())) {
            fileUtils.deleteFile(mediaManager.getImg());
          } else if (ObjectUtils.isEmpty(mediaManager.getImg()) && ObjectUtils.isNotEmpty(
              mediaManager.getVideo())) {
            fileUtils.deleteFile(mediaManager.getVideo());
          }
        }
      }
    }

    return 1;
  }
}
