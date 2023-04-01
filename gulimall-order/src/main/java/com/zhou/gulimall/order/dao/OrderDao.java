package com.zhou.gulimall.order.dao;

import com.zhou.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author zhouhr
 * @email zhou@gmail.com
 * @date 2022-12-17 13:50:13
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
