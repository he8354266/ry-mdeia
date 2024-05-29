package com.ruoyi.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.domain.MediaManager;
import com.ruoyi.system.domain.WaterfallInfo;
import java.util.List;

/**
 * mediaService接口
 * 
 * @author ruoyi
 * @date 2024-05-28
 */
public interface IMediaManagerService extends IService<MediaManager>
{
    /**
     * 查询media
     * 
     * @param id media主键
     * @return media
     */
    public MediaManager selectRyMediaManagerById(Long id);

    /**
     * 查询media列表
     * 
     * @param ryMediaManager media
     * @return media集合
     */
    public List<MediaManager> selectRyMediaManagerList(MediaManager ryMediaManager);

    /**
     * 新增media
     * 
     * @param ryMediaManager media
     * @return 结果
     */
    public int insertRyMediaManager(MediaManager ryMediaManager);

    /**
     * 修改media
     * 
     * @param ryMediaManager media
     * @return 结果
     */
    public int updateRyMediaManager(MediaManager ryMediaManager);

    /**
     * 批量删除media
     * 
     * @param ids 需要删除的media主键集合
     * @return 结果
     */
    public int deleteRyMediaManagerByIds(Long[] ids);

    /**
     * 删除media信息
     * 
     * @param id media主键
     * @return 结果
     */
    public int deleteRyMediaManagerById(Long id);
}
