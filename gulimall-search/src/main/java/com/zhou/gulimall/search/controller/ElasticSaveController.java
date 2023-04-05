package com.zhou.gulimall.search.controller;

import com.zhou.common.exception.BizCodeErume;
import com.zhou.common.to.es.SkuEsModel;
import com.zhou.common.utils.R;
import com.zhou.gulimall.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequestMapping("/search/save")
@RestController
public class ElasticSaveController {

    @Autowired
    ProductSaveService productSaveService;

    //上架商品
    @PostMapping("/product")
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModelList){
        boolean isOk = false;
        try {
            isOk = productSaveService.productStatusUp(skuEsModelList);
        }catch (Exception e){
            log.error("商品上架错误:{}", e);
            return R.error(BizCodeErume.PRODUCT_UP_EXCEPTION.getCode(), BizCodeErume.PRODUCT_UP_EXCEPTION.getMsg());
        }
        if(isOk){
            return R.ok();
        }else {
            return R.error(BizCodeErume.PRODUCT_UP_EXCEPTION.getCode(), BizCodeErume.PRODUCT_UP_EXCEPTION.getMsg());
        }

    }
}
