package com.zhou.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhou.common.utils.PageUtils;
import com.zhou.gulimall.order.entity.UndoLogEntity;

import java.util.Map;

/**
 * 
 *
 * @author zhouhr
 * @email zhou@gmail.com
 * @date 2022-12-17 13:50:13
 */
public interface UndoLogService extends IService<UndoLogEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

