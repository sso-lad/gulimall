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

    List<SkuImagesEntity> images;

    List<SkuItemSaleAttrVo> saleAttr;

    SpuInfoDescEntity desc;

    List<SpuItemAttrGroupVo> groupAttrs;


    @Data
    public static class SkuItemSaleAttrVo{

        private Long attrId;
        private String attrName;
        private List<String> attrValues;

    }
    @Data
    public static class SpuItemAttrGroupVo{
        private String groupName;
        private List<SpuBaseAttrVo> attrs;

    }
    @Data
    public static class SpuBaseAttrVo{
        private String attrName;
        private String attrValue;
    }
}
