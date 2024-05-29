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



  int removeIds(List<Long> list);
}
