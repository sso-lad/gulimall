package com.zhou.gulimall.search.config;


import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootConfiguration
@Configuration
public class GulimallElasticSearchConfig {

    public   static  final RequestOptions COMMON_OPTIONS;
    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
       COMMON_OPTIONS = builder.build();
    }

    @Bean
    public RestHighLevelClient init(){
       RestHighLevelClient client =  new RestHighLevelClient(RestClient.builder(
                new HttpHost("192.168.241.67",9200,"http"))
                );
       return client;
    }

}
