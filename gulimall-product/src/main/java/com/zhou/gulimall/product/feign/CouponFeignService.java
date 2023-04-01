package com.zhou.gulimall.product.feign;

import com.zhou.common.to.SkuReductionTo;
import com.zhou.common.to.SpuBoundTo;
import com.zhou.common.utils.R;
import oracle.jdbc.proxy.annotation.Post;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("gulimall-coupon")
public interface CouponFeignService {

    @PostMapping("/coupon/spubounds/save")
    R saveSpuBounds(@RequestBody SpuBoundTo spuBoundTo);

    @PostMapping("/coupon/skufullreduction/saveInfo")
    R saveSkuReduction(@RequestBody SkuReductionTo skuReductionTo);


}
