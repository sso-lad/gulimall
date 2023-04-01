package com.atguigu.gulimall.thirdparty;

import com.aliyun.oss.OSSClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@SpringBootTest
class GulimallThirdPartyApplicationTests {


    @Autowired
    OSSClient ossClient;
    @Test
    void contextLoads() throws FileNotFoundException {
        InputStream inputStream =  new FileInputStream("C:\\Users\\15913\\Pictures\\Camera Roll\\123.png");
        ossClient.putObject("gulimall-zhouhr", "123.png", inputStream);
        ossClient.shutdown();
        System.out.println("上传成功");
    }

}
