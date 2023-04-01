package com.zhou.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhou.common.to.SkuReductionTo;
import com.zhou.common.utils.PageUtils;
import com.zhou.gulimall.coupon.entity.SkuFullReductionEntity;

import java.util.Map;

/**
 * 商品满减信息
 *
 * @author zhouhr
 * @email zhou@gmail.com
 * @date 2022-11-07 23:21:55
 */
public interface SkuFullReductionService extends IService<SkuFullReductionEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSkuReduction(SkuReductionTo skuReductionTo);
}

