package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.PwdManager;
import com.ruoyi.system.domain.WaterfallInfo;
import java.util.List;

/**
 * waterfallMapper接口
 * 
 * @author ruoyi
 * @date 2024-02-28
 */
public interface WaterfallInfoMapper  extends BaseMapper<WaterfallInfo>
{
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
     * 删除waterfall
     *
     * @param id waterfall主键
     * @return 结果
     */
    public int deleteWaterfallInfoById(Long id);

    /**
     * 批量删除waterfall
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWaterfallInfoByIds(Long[] ids);
}
