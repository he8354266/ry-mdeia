package com.ruoyi.system.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.util.Date;
import java.util.List;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * waterfall对象 ry_waterfall_info
 *
 * @author ruoyi
 * @date 2024-02-28
 */
@Data
@TableName("ry_waterfall_info")
public class WaterfallInfo {

  private static final long serialVersionUID = 1L;

  /**
   * 编号
   */
  @JsonSerialize(using = ToStringSerializer.class)
  private Long id;

  /**
   * 编号
   */
  @Excel(name = "编号")
  private String number;

  /**
   * 昵称
   */
  @Excel(name = "昵称")
  private String nickName;


  /**
   * 封面图片
   */
  @Excel(name = "封面图片")
  private String titleImg;


  /**
   * 详情图片数量
   */
  @TableField(exist = false)
  private int imgNum;
  /**
   * 详情视频数量
   */
  @TableField(exist = false)
  private int videoNum;

  /**
   * 详情图片
   */
  @TableField(exist = false)
  private List<MediaManager> imgList;
  /**
   * 详情视频
   */
  @TableField(exist = false)
  private List<MediaManager> videoList;

  /**
   * 创建者
   */
  private String createBy;

  /**
   * 创建时间
   */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createTime;

  /**
   * 更新者
   */
  private String updateBy;

  /**
   * 更新时间
   */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date updateTime;

  /**
   * 备注
   */
  private String remark;
}
