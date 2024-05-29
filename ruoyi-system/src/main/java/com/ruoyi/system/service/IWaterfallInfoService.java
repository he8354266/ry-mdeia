package com.ruoyi.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.domain.PwdManager;
import com.ruoyi.system.domain.WaterfallInfo;
import java.util.Arrays;
import java.util.List;

/**
 * waterfallService接口
 *
 * @author ruoyi
 * @date 2024-02-28
 */
public interface IWaterfallInfoService extends IService<WaterfallInfo> {

  /**
   * 查询waterfall
   *
   * @param id waterfall主键
   * @return waterfall
   */
  public WaterfallInfo selectWaterfallInfoById(Long id);

  /**
   * 查询waterfall列表
   *
   * @param waterfallInfo waterfall
   * @return waterfall集合
   */
  public List<WaterfallInfo> selectWaterfallInfoList(WaterfallInfo waterfallInfo);

  /**
   * 新增waterfall
   *
   * @param waterfallInfo waterfall
   * @return 结果
   */
  public int insertWaterfallInfo(WaterfallInfo waterfallInfo);

  /**
   * 修改waterfall
   *
   * @param waterfallInfo waterfall
   * @return 结果
   */
  public int updateWaterfallInfo(WaterfallInfo waterfallInfo);

  /**
   * 批量删除waterfall
   *
   * @param ids 需要删除的waterfall主键集合
   * @return 结果
   */
  public int deleteWaterfallInfoByIds(Long[] ids);

  /**
   * 删除waterfall信息
   *
   * @param id waterfall主键
   * @return 结果
   */
  public int deleteWaterfallInfoById(Long id);

  int removeIds(List<Long> list);
}
