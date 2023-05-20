package com.zhou.gulimall.search.feign;


import com.zhou.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name="gulimall-product")
public interface ProductFeignService {

    @GetMapping("/product/attr/info/{attrId}")
    public R attrInfo(@PathVariable("attrId") Long attrId);

    @GetMapping("/product/brand/brandsInfo")
    public R brandsInfo(@RequestParam("brandIds") List<Long> brandIds);
}
