package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.system.domain.MediaManager;
import com.ruoyi.system.domain.WaterfallInfo;
import com.ruoyi.system.mapper.MediaManagerMapper;
import com.ruoyi.system.mapper.WaterfallInfoMapper;
import com.ruoyi.system.service.IMediaManagerService;
import com.ruoyi.system.service.IWaterfallInfoService;
import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * mediaService业务层处理
 * 
 * @author ruoyi
 * @date 2024-05-28
 */
@Service
public class MediaManagerServiceImpl extends ServiceImpl<MediaManagerMapper, MediaManager> implements IMediaManagerService
{
    @Resource
    private MediaManagerMapper MediaManagerMapper;

    /**
     * 查询media
     * 
     * @param id media主键
     * @return media
     */
    @Override
    public MediaManager selectRyMediaManagerById(Long id)
    {
        return MediaManagerMapper.selectRyMediaManagerById(id);
    }

    /**
     * 查询media列表
     * 
     * @param ryMediaManager media
     * @return media
     */
    @Override
    public List<MediaManager> selectRyMediaManagerList(MediaManager ryMediaManager)
    {
        return MediaManagerMapper.selectRyMediaManagerList(ryMediaManager);
    }

    /**
     * 新增media
     * 
     * @param ryMediaManager media
     * @return 结果
     */
    @Override
    public int insertRyMediaManager(MediaManager ryMediaManager)
    {
        return MediaManagerMapper.insertRyMediaManager(ryMediaManager);
    }

    /**
     * 修改media
     * 
     * @param ryMediaManager media
     * @return 结果
     */
    @Override
    public int updateRyMediaManager(MediaManager ryMediaManager)
    {
        return MediaManagerMapper.updateRyMediaManager(ryMediaManager);
    }

    /**
     * 批量删除media
     * 
     * @param ids 需要删除的media主键
     * @return 结果
     */
    @Override
    public int deleteRyMediaManagerByIds(Long[] ids)
    {
        return MediaManagerMapper.deleteRyMediaManagerByIds(ids);
    }

    /**
     * 删除media信息
     * 
     * @param id media主键
     * @return 结果
     */
    @Override
    public int deleteRyMediaManagerById(Long id)
    {
        return MediaManagerMapper.deleteRyMediaManagerById(id);
    }
}
