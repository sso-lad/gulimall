package com.zhou.gulimall.search.service;




import com.zhou.common.to.es.SkuEsModel;

import java.io.IOException;
import java.util.List;

public interface ProductSaveService {
    public boolean productStatusUp(List<SkuEsModel> skuEsModelList) throws IOException;
}
