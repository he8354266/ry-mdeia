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

  /**
   * 查询waterfall
   *
   * @param id waterfall主键
   * @return waterfall
   */
  @Override
  public WaterfallInfo selectWaterfallInfoById(Long id) {
    return waterfallInfoMapper.selectWaterfallInfoById(id);
  }

  /**
   * 查询waterfall列表
   *
   * @param waterfallInfo waterfall
   * @return waterfall
   */
  @Override
  public List<WaterfallInfo> selectWaterfallInfoList(WaterfallInfo waterfallInfo) {
    return waterfallInfoMapper.selectWaterfallInfoList(waterfallInfo);
  }

  /**
   * 新增waterfall
   *
   * @param waterfallInfo waterfall
   * @return 结果
   */
  @Override
  public int insertWaterfallInfo(WaterfallInfo waterfallInfo) {
    waterfallInfo.setCreateTime(DateUtils.getNowDate());
    return waterfallInfoMapper.insertWaterfallInfo(waterfallInfo);
  }

  /**
   * 修改waterfall
   *
   * @param waterfallInfo waterfall
   * @return 结果
   */
  @Override
  public int updateWaterfallInfo(WaterfallInfo waterfallInfo) {
    waterfallInfo.setUpdateTime(DateUtils.getNowDate());
    return waterfallInfoMapper.updateWaterfallInfo(waterfallInfo);
  }

  /**
   * 批量删除waterfall
   *
   * @param ids 需要删除的waterfall主键
   * @return 结果
   */
  @Override
  public int deleteWaterfallInfoByIds(Long[] ids) {
    return waterfallInfoMapper.deleteWaterfallInfoByIds(ids);
  }

  /**
   * 删除waterfall信息
   *
   * @param id waterfall主键
   * @return 结果
   */
  @Override
  public int deleteWaterfallInfoById(Long id) {
    return waterfallInfoMapper.deleteWaterfallInfoById(id);
  }

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
