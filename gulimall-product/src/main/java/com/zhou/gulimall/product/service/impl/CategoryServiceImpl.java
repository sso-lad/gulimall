package com.zhou.gulimall.product.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zhou.gulimall.product.service.CategoryBrandRelationService;
import com.zhou.gulimall.product.vo.Catelog2Vo;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhou.common.utils.PageUtils;
import com.zhou.common.utils.Query;

import com.zhou.gulimall.product.dao.CategoryDao;
import com.zhou.gulimall.product.entity.CategoryEntity;
import com.zhou.gulimall.product.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {


    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RedissonClient redissonClient;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        //查出所有分类
        List<CategoryEntity> entities = baseMapper.selectList(null);

        //组装成父子的树形结构

        //找到所有的一级分类
        List<CategoryEntity> collect = entities.stream().filter(categoryEntity -> {
            return categoryEntity.getParentCid() == 0;
        }).map(menu-> {
            menu.setChildren(getChildrens(menu, entities));
            return menu;
        }).sorted((menu1,menu2)->{
            return (menu1.getSort()==null?0:menu1.getSort()) -(menu2.getSort()==null?0:menu2.getSort());
        }).collect(Collectors.toList());

        return collect;
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {
        //检查删除的菜单，是否被其他的地方引用

        baseMapper.deleteBatchIds(asList);
    }

    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> paths = new ArrayList<>();
        List<Long> parentPath = findParentPath(catelogId, paths);
        Collections.reverse(parentPath);
        return (Long[]) parentPath.toArray(new Long[parentPath.size()]);
    }

    /**
     * 级联更新所有关联
     * @param category
     */
//    @Caching(evict = {
//            @CacheEvict(value = {"category"},key = "'getLevel1Categorys'"),
//            @CacheEvict(value = {"category"},key = "'getCatalogJson'"),
//    })
    @CacheEvict(value = {"category"},allEntries = true)//失效模式
    @CachePut//雙寫模式
    @Transactional
    @Override
    public void updateCasecade(CategoryEntity category) {
        this.updateById(category);
        categoryBrandRelationService.updateCategory(category.getCatId(),category.getName());
    }
    //每一个需要缓存的数据我们都来指定要放到那个名字的缓存。【缓存的分区（按照业务类型分）】
    @Cacheable(value = {"category"},key = "#root.method.name",sync = true) //代表当前方法的结果需要缓存
    @Override
    public List<CategoryEntity> getLevel1Categorys() {
        System.out.println("getLevel1Categorys...");
        List<CategoryEntity> entities = this.baseMapper.selectList(
                new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
        return  entities;
    }
    public Map<String, List<Catelog2Vo>> getCataogJsonFromDB(){
        synchronized (this){
            //得到锁以后,我们应该再去缓存中确定一次，如果没有才需要继续查询
            String catalogJSON = redisTemplate.opsForValue().get("catalogJSON");
            if(!StringUtils.isEmpty(catalogJSON)){
                Map<String, List<Catelog2Vo>> result = JSON.parseObject(
                        catalogJSON,new TypeReference<Map<String, List<Catelog2Vo>>>(){});
                return  result;
            }
            //TODO 本地锁： sychronized,juc(lock) 进程锁，在分布式情况下，想要锁住所有，必须使用分布式锁
            System.out.println("查询了数据库.....");
            /**
             * 1.将数据多次查询变为一步
             */
            List<CategoryEntity> selectList = baseMapper.selectList(null);
            //查出所有1级分类
            List<CategoryEntity> level1Categorys = getParentCid(selectList, 0L);
            //封装数据
            Map<String, List<Catelog2Vo>> parend_cid = level1Categorys.stream().collect(Collectors.toMap(k -> {
                return k.getCatId().toString();
            }, v -> {
                List<CategoryEntity> entities = getParentCid(selectList,v.getCatId());
                List<Catelog2Vo> catelog2Vos = null;
                if (entities != null) {
                    catelog2Vos = entities.stream().map(l2 -> {
                        Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null,
                                l2.getCatId().toString(), l2.getName());
                        //找当前二级分类的三级分类封装成vo
                        List<CategoryEntity> level3Catelog =
                                getParentCid(selectList,l2.getCatId());
                        if(level1Categorys != null){
                            List<Catelog2Vo.Catelog3Vo> catelog3List = level3Catelog.stream().map(l3 -> {
                                Catelog2Vo.Catelog3Vo catelog3Vo = new Catelog2Vo.Catelog3Vo(l2.getCatId().toString()
                                        ,l3.getCatId().toString(),l3.getName());
                                return catelog3Vo;
                            }).collect(Collectors.toList());
                            //封装成指定格式
                            catelog2Vo.setCatalog3List(catelog3List);
                        }
                        return catelog2Vo;
                    }).collect(Collectors.toList());
                }

                return catelog2Vos;
            }));
            //3.db放入缓存
            String s = JSON.toJSONString(parend_cid);
            redisTemplate.opsForValue().set("catalogJSON", s,1, TimeUnit.DAYS);
            return parend_cid;
        }
    }

    /**
     * 缓存里面的数据如何和数据库保持一致
     * 1.双写模式
     * 2.失效模式
     * @return
     */
    public Map<String, List<Catelog2Vo>> getCataogJsonFromDBWithRedissionLock(){
        //1.锁的名字。锁的粒度，越细越快。
        //锁的粒度：具体缓存的是某个数据，11-号商品；
        RLock lock = redissonClient.getLock("catalogJson-lock");
        //默认看门狗机制,自动续锁
        lock.lock();
        System.out.println("获取锁成功");
        //加锁成功、执行业务
        Map<String, List<Catelog2Vo>> dataFromDb;
        try {
            dataFromDb = getDataFromDb();
        } finally {
            lock.unlock();
        }
        return dataFromDb;
    }
    public Map<String, List<Catelog2Vo>> getCataogJsonFromDBWithRedisLock(){
        //1.占分布式锁.
        //原子操作
        String uuid = UUID.randomUUID().toString();
        Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock", uuid,30,TimeUnit.SECONDS);
        if (lock){
            //加锁成功、执行业务
            Map<String, List<Catelog2Vo>> dataFromDb;
            try {
                 dataFromDb = getDataFromDb();
            } finally {
                //原子删锁
                String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
                Long lock1 = redisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class), Arrays.asList("lock"), uuid);
            }
            return dataFromDb;
        } else {
            //自选
            try {
                Thread.sleep(200);
            }catch (Exception e){

            }
            return getCataogJsonFromDBWithRedisLock();
        }
    }
    private Map<String,List<Catelog2Vo>> getDataFromDb() {
        //得到锁以后,我们应该再去缓存中确定一次，如果没有才需要继续查询
        String catalogJSON = redisTemplate.opsForValue().get("catalogJSON");
        if (!StringUtils.isEmpty(catalogJSON)) {
            Map<String, List<Catelog2Vo>> result = JSON.parseObject(
                    catalogJSON, new TypeReference<Map<String, List<Catelog2Vo>>>() {
                    });
            return result;
        }
        //TODO 本地锁： sychronized,juc(lock) 进程锁，在分布式情况下，想要锁住所有，必须使用分布式锁
        System.out.println("查询了数据库.....");
        /**
         * 1.将数据多次查询变为一步
         */
        List<CategoryEntity> selectList = baseMapper.selectList(null);
        //查出所有1级分类
        List<CategoryEntity> level1Categorys = getParentCid(selectList, 0L);
        //封装数据
        Map<String, List<Catelog2Vo>> parend_cid = level1Categorys.stream().collect(Collectors.toMap(k -> {
            return k.getCatId().toString();
        }, v -> {
            List<CategoryEntity> entities = getParentCid(selectList, v.getCatId());
            List<Catelog2Vo> catelog2Vos = null;
            if (entities != null) {
                catelog2Vos = entities.stream().map(l2 -> {
                    Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null,
                            l2.getCatId().toString(), l2.getName());
                    //找当前二级分类的三级分类封装成vo
                    List<CategoryEntity> level3Catelog =
                            getParentCid(selectList, l2.getCatId());
                    if (level1Categorys != null) {
                        List<Catelog2Vo.Catelog3Vo> catelog3List = level3Catelog.stream().map(l3 -> {
                            Catelog2Vo.Catelog3Vo catelog3Vo = new Catelog2Vo.Catelog3Vo(l2.getCatId().toString()
                                    , l3.getCatId().toString(), l3.getName());
                            return catelog3Vo;
                        }).collect(Collectors.toList());
                        //封装成指定格式
                        catelog2Vo.setCatalog3List(catelog3List);
                    }
                    return catelog2Vo;
                }).collect(Collectors.toList());
            }

            return catelog2Vos;
        }));
        //3.db放入缓存
        String s = JSON.toJSONString(parend_cid);
        redisTemplate.opsForValue().set("catalogJSON", s, 1, TimeUnit.DAYS);
        return parend_cid;
    }
    @Cacheable(value = "category",key = "#root.method.name",sync = true)
    @Override
    public Map<String, List<Catelog2Vo>> getCatalogJson(){
        System.out.println("查詢了數據庫");
        /**
         * 1.将数据多次查询变为一步
         */
        List<CategoryEntity> selectList = baseMapper.selectList(null);
        //查出所有1级分类
        List<CategoryEntity> level1Categorys = getParentCid(selectList, 0L);
        //封装数据
        Map<String, List<Catelog2Vo>> parend_cid = level1Categorys.stream().collect(Collectors.toMap(k -> {
            return k.getCatId().toString();
        }, v -> {
            List<CategoryEntity> entities = getParentCid(selectList, v.getCatId());
            List<Catelog2Vo> catelog2Vos = null;
            if (entities != null) {
                catelog2Vos = entities.stream().map(l2 -> {
                    Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null,
                            l2.getCatId().toString(), l2.getName());
                    //找当前二级分类的三级分类封装成vo
                    List<CategoryEntity> level3Catelog =
                            getParentCid(selectList, l2.getCatId());
                    if (level1Categorys != null) {
                        List<Catelog2Vo.Catelog3Vo> catelog3List = level3Catelog.stream().map(l3 -> {
                            Catelog2Vo.Catelog3Vo catelog3Vo = new Catelog2Vo.Catelog3Vo(l2.getCatId().toString()
                                    , l3.getCatId().toString(), l3.getName());
                            return catelog3Vo;
                        }).collect(Collectors.toList());
                        //封装成指定格式
                        catelog2Vo.setCatalog3List(catelog3List);
                    }
                    return catelog2Vo;
                }).collect(Collectors.toList());
            }

            return catelog2Vos;
        }));
        return parend_cid;
    }

    //1.升级lettuce客户端 2.切换使用jedis
    public Map<String, List<Catelog2Vo>> getCataogJson2() {
        /**
         * 1.空结果缓存,解决缓存穿透
         * 2.设置过期时间（随机值）,解决缓存雪崩
         * 3.加锁解决缓存击穿问题
         */

        //1.加入缓存
        String catalogJSON = redisTemplate.opsForValue().get("catalogJSON");
        if(StringUtils.isEmpty(catalogJSON)){
            System.out.println("缓存不命中....查询数据库...");
            //2.缓存中没有
            Map<String, List<Catelog2Vo>> cataogJsonFromDB = getCataogJsonFromDBWithRedissionLock();
            System.out.println("释放锁");
            return cataogJsonFromDB;
        }
        System.out.println("缓存命中....直接返回...");
        Map<String, List<Catelog2Vo>> result = JSON.parseObject(
                catalogJSON,new TypeReference<Map<String, List<Catelog2Vo>>>(){});
        return  result;
    }
    private List<CategoryEntity> getParentCid(List<CategoryEntity> selectList,Long parentCid){
        List<CategoryEntity> collect = selectList.stream().
                filter(item -> item.getParentCid() == parentCid).collect(Collectors.toList());
        return collect;
    }

    //225,25,2
    private List<Long> findParentPath(Long catelogId,List<Long> paths){
        //1.收集当前节点id
        paths.add(catelogId);
        CategoryEntity byId = this.getById(catelogId);
        if(byId.getParentCid()!=0){
            findParentPath(byId.getParentCid(),paths);
        }
        return paths;
    }

    /**
     * 递归查找所有菜单的子菜单
     * @param root
     * @param all
     * @return
     */
    private List<CategoryEntity> getChildrens(CategoryEntity root,List<CategoryEntity> all){
        List<CategoryEntity> children = all.stream().filter(categoryEntity -> {
            return categoryEntity.getParentCid().equals(root.getCatId());
        }).map(categoryEntity -> {
            categoryEntity.setChildren(getChildrens(categoryEntity, all));
            return categoryEntity;
        }).sorted((menu1,menu2)->{
            return (menu1.getSort()==null?0:menu1.getSort()) -(menu2.getSort()==null?0:menu2.getSort());
        }).collect(Collectors.toList());
        return children;
    }
}