package com.zhou.gulimall.member.dao;

import com.zhou.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author zhouhr
 * @email zhou@gmail.com
 * @date 2022-12-17 13:32:01
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
