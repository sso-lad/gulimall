package com.zhou.gulimall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.zhou.gulimall.product.entity.AttrEntity;
import com.zhou.gulimall.product.service.AttrAttrgroupRelationService;
import com.zhou.gulimall.product.service.AttrService;
import com.zhou.gulimall.product.service.CategoryService;
import com.zhou.gulimall.product.vo.AttrGroupRelationVo;
import com.zhou.gulimall.product.vo.AttrGroupWithAttrsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.zhou.gulimall.product.entity.AttrGroupEntity;
import com.zhou.gulimall.product.service.AttrGroupService;
import com.zhou.common.utils.PageUtils;
import com.zhou.common.utils.R;



/**
 * 属性分组
 *
 * @author zhouhr
 * @email zhou@gmail.com
 * @date 2022-11-01 00:38:30
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AttrService attrService;

    @Autowired
    private AttrAttrgroupRelationService relationService;

    @PostMapping("/attr/relation")
    public R addRelation(@RequestBody List<AttrGroupRelationVo> vos){
        relationService.saveBatch(vos);
        return R.ok();
    }

    @GetMapping("/{catelogId}/withattr")
    public R getAttrGroupWithAttrs(@PathVariable("catelogId") Long catelogId){
        //1.查出当前分类下的所有属性分组
        //2.查出每个属性分组所有属性
        List<AttrGroupWithAttrsVo> vos= attrGroupService.getAttrGroupWithAttrsByCatelogId(catelogId);

        return R.ok().put("data", vos);
    }

    /**
     * 查询所有分组关联的
     * @param attrgroupId
     * @return
     */
    @GetMapping("/{attrgroupId}/attr/relation")
    public  R attrRelation(@PathVariable("attrgroupId") Long attrgroupId){
       List<AttrEntity> entityList = attrService.getRelationAttr(attrgroupId);
       return R.ok().put("data", entityList);
    }

    /**
     * 查询没有关联的分组
     * @param attrgroupId
     * @return
     */
    @GetMapping("/{attrgroupId}/noattr/relation")
    public  R attrNoRelation(@PathVariable("attrgroupId") Long attrgroupId,
                             @RequestParam Map<String,Object> params){
        PageUtils page =  attrService.getNoRelationAttr(params,attrgroupId);
        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/list/{catelogId}")
    public R list(@RequestParam Map<String, Object> params,@PathVariable("catelogId") Long catelogId){
        PageUtils page = attrGroupService.queryPage(params,catelogId);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
		Long catelogId = attrGroup.getCatelogId();
		Long[] path = categoryService.findCatelogPath(catelogId);
		attrGroup.setCatelogPath(path);
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }


    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

    /**
     * 批量删除
     * @param vos
     * @return
     */
    @PostMapping("/attr/relation/delete")
    public R deleteRelation(@RequestBody AttrGroupRelationVo[] vos){
        attrService.deleteRelation(vos);
        return R.ok();
    }
}
