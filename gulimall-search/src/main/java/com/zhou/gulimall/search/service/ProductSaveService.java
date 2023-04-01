package com.zhou.gulimall.search.service;

import com.zhou.common.to.es.SkuEsModel;

import java.util.List;

public interface ProductSaveService {
    public void productStatusUp(List<SkuEsModel> skuEsModelList);
}
