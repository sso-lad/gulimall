package com.zhou.gulimall.order.dao;

import com.zhou.gulimall.order.entity.UndoLogEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * 
 * @author zhouhr
 * @email zhou@gmail.com
 * @date 2022-12-17 13:50:13
 */
@Mapper
public interface UndoLogDao extends BaseMapper<UndoLogEntity> {
	
}
