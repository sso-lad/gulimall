package com.zhou.gulimall.product.dao;

import com.zhou.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author zhouhr
 * @email zhou@gmail.com
 * @date 2022-11-01 00:38:30
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
