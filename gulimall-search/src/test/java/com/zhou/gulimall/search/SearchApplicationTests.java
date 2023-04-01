package com.zhou.gulimall.search;

import com.alibaba.fastjson.JSON;
import com.zhou.gulimall.search.config.GulimallElasticSearchConfig;
import io.swagger.models.auth.In;
import lombok.Data;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;


import org.elasticsearch.common.xcontent.XContent;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class SearchApplicationTests {

    @Autowired
    private RestHighLevelClient restHighLevelClient;
    @Test
    public void contextLoads() {
        System.out.println(restHighLevelClient);

    }
    @Test
    public void searchIndex() throws Exception{
        SearchRequest request = new SearchRequest();
        //指定索引
        request.indices("users");
        //指定DSL,检索条件
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //构造检索条件
        sourceBuilder.query(QueryBuilders.matchQuery("username", "zhangsan"));
//        sourceBuilder.from();
//        sourceBuilder.size();
//        sourceBuilder.aggregation();
        request.source(sourceBuilder);
        //执行检索
        SearchResponse search = restHighLevelClient.search(request, GulimallElasticSearchConfig.COMMON_OPTIONS);

        //分析结果
        System.out.println(search.toString());

    }
    //测试es存储数据
    @Test
    public void initIndex() throws Exception{
        IndexRequest indexRequest = new IndexRequest("users");
        indexRequest.id("1");
//        indexRequest.source("userName","张三","age",18,"gender","男");
        User user = new  User();
        user.setAge(18);
        user.setGender("man");
        user.setUsername("zhangsan");
        String s = JSON.toJSONString(user);
        indexRequest.source(s, XContentType.JSON);
        //执行操作
        IndexResponse index = restHighLevelClient.index(indexRequest, GulimallElasticSearchConfig.COMMON_OPTIONS);
        //提取响应数据
        System.out.println(index);

    }
    @Data
    class User{
        private String username;
        private String gender;
        private Integer age;
    }

}
