package com.zhou.gulimall.product.vo;

import com.zhou.gulimall.product.entity.SkuImagesEntity;
import com.zhou.gulimall.product.entity.SkuInfoEntity;
import com.zhou.gulimall.product.entity.SkuSaleAttrValueEntity;
import com.zhou.gulimall.product.entity.SpuInfoDescEntity;
import lombok.Data;

import java.util.List;
@Data
public class SkuItemVo {

    SkuInfoEntity info;

    boolean hasStock = true;

    List<SkuImagesEntity> images;

    List<SkuItemSaleAttrVo> saleAttr;

    SpuInfoDescEntity desc;

    List<SpuItemAttrGroupVo> groupAttrs;

}
