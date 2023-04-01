package com.zhou.gulimall.search.service.impl;

import com.zhou.common.to.es.SkuEsModel;
import com.zhou.gulimall.search.service.ProductSaveService;
import org.elasticsearch.client.ElasticsearchClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProductSaveServiceImpl implements ProductSaveService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public void productStatusUp(List<SkuEsModel> skuEsModelList) {

        //保存到es
        //1.给es中建立索引.product
    }
}
