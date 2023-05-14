package com.zhou.gulimall.search.service;

import com.zhou.gulimall.search.vo.SearchParam;
import com.zhou.gulimall.search.vo.SearchResult;

public interface MallSearchService {
    /**
     * 检索的所有参数
     * @param param
     * @return 返回检索的结果
     */
    SearchResult search(SearchParam param);
}
