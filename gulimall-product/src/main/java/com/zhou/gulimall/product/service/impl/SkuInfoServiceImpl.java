package com.zhou.gulimall.product.service.impl;

import com.zhou.gulimall.product.entity.SkuImagesEntity;
import com.zhou.gulimall.product.entity.SpuInfoDescEntity;
import com.zhou.gulimall.product.service.AttrGroupService;
import com.zhou.gulimall.product.service.SkuImagesService;
import com.zhou.gulimall.product.service.SpuInfoDescService;
import com.zhou.gulimall.product.vo.SkuItemVo;
import com.zhou.gulimall.product.vo.SpuItemAttrGroupVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhou.common.utils.PageUtils;
import com.zhou.common.utils.Query;

import com.zhou.gulimall.product.dao.SkuInfoDao;
import com.zhou.gulimall.product.entity.SkuInfoEntity;
import com.zhou.gulimall.product.service.SkuInfoService;
import org.springframework.util.StringUtils;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Autowired
    SkuImagesService imagesService;

    @Autowired
    private SpuInfoDescService spuInfoDescService;

    @Autowired
    private AttrGroupService attrGroupService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSkuInfo(SkuInfoEntity skuInfoEntity) {
        this.baseMapper.insert(skuInfoEntity);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SkuInfoEntity> qw = new QueryWrapper<>();
        String key = (String) params.get("key");
        if(!StringUtils.isEmpty(key)){
            qw.and(w->{
                w.eq("sku_id", key).or().like("sku_name", key);
            });
        }
        String cateLogId = (String) params.get("cateLogId");
        if(!StringUtils.isEmpty(cateLogId)&&!"0".equalsIgnoreCase(cateLogId)){

            qw.eq("cataLog_id", cateLogId);
        }
        String brandId = (String) params.get("brandId");
        if(!StringUtils.isEmpty(brandId)&&!"0".equalsIgnoreCase(brandId)){
                 qw.eq("brand_id", brandId);
        }
        String min = (String) params.get("min");
        if(!StringUtils.isEmpty(min)){
                qw.ge("price", min);
        }
        String max = (String) params.get("max");

        if(!StringUtils.isEmpty(max)){
            try{
                BigDecimal bigDecimal = new BigDecimal(max);
                if(bigDecimal.compareTo(new BigDecimal("0")) == 1) {
                    qw.le("price", max);
                }
            }catch (Exception e){

            }

        }
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                qw
        );

        return new PageUtils(page);
    }

    @Override
    public List<SkuInfoEntity> getSkusBySpuId(Long spuId) {

        List<SkuInfoEntity> list = this.list(new QueryWrapper<SkuInfoEntity>().eq("spu_id", spuId));
        return list;
    }

    @Override
    public SkuItemVo item(Long skuId) {
        SkuItemVo skuItemVo = new SkuItemVo();
        //1.sku基本信息 pms_sku_info
        SkuInfoEntity info = getById(skuId);
        skuItemVo.setInfo(info);
        //2.sku图片信息 pms_sku_images
        List<SkuImagesEntity> images =  imagesService.getImagesBySkuId(skuId);
        skuItemVo.setImages(images);
        //3.获取spu的销售属性组合

        //4.获取spu的介绍
        Long spuId = info.getSpuId();
        SpuInfoDescEntity desc = spuInfoDescService.getById(spuId);
        skuItemVo.setDesc(desc);
        //5.获取spu的规格参数信息
        Long catalogId = info.getCatalogId();
        List<SpuItemAttrGroupVo> attrGroupVos = attrGroupService.getAttrGroupWithAttrsBySpuId(spuId,catalogId);
        skuItemVo.setGroupAttrs(attrGroupVos);
        return null;
    }

}