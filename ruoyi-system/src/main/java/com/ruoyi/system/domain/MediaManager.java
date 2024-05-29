package com.ruoyi.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * media对象 ry_media_manager
 * 
 * @author ruoyi
 * @date 2024-05-28
 */
@Data
@TableName("ry_media_manager")
public class MediaManager
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /** 详情信息ID */
    @Excel(name = "详情信息ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long waterfallId;

    /** 图片 */
    @Excel(name = "图片")
    private String img;

    /** 视频 */
    @Excel(name = "视频")
    private String video;


}
