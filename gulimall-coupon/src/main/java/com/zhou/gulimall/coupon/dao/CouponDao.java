package com.zhou.gulimall.coupon.dao;

import com.zhou.gulimall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author zhouhr
 * @email zhou@gmail.com
 * @date 2022-11-07 23:21:55
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
