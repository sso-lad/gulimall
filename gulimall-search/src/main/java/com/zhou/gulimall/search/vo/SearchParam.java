package com.zhou.gulimall.search.vo;

import lombok.Data;

import java.util.List;

/**
 * 封装页面所有可能传递过来的查询条件
 */
@Data
public class SearchParam {

    private String keyword;//页面传递过来的全文匹配关键字
    private String catalog3Id;//三级分类id
    private String sort;
    private Integer hasStock;
    private String skuPrice;
    private List<Long> brandId;//品牌
    private List<String> attrs;//属性
    private Integer pageNum = 1;
}
