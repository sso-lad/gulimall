package com.zhou.gulimall.product.config;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.*;

@Configuration
public class MyThreadConfig {
    @Bean
    public ThreadPoolExecutor threadPoolExecutor(ThreadPoolConfigProperties pool){
        return new ThreadPoolExecutor(pool.getCoreSize(), pool.getMaxSize(),
                pool.getKeepAliveTime(), TimeUnit.SECONDS,new LinkedBlockingQueue<>(10000),
                Executors.defaultThreadFactory(),new ThreadPoolExecutor.AbortPolicy());
    }
}
