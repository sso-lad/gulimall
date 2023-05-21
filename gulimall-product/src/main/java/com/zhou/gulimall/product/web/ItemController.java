package com.zhou.gulimall.product.web;

import com.zhou.gulimall.product.service.SkuInfoService;
import com.zhou.gulimall.product.service.impl.SkuInfoServiceImpl;
import com.zhou.gulimall.product.vo.SkuItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ItemController {

    @Autowired
    private SkuInfoService skuInfoService;

    /**
     * 展示当前sku的详情
     * @param skuId
     * @return
     */
    @GetMapping("/{skuId}.html")
    public String skuItem(@PathVariable("skuId") Long skuId){
        SkuItemVo skuItemVo = skuInfoService.item(skuId);
        return "item";
    }
}