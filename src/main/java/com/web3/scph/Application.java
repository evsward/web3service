package com.web3.scph;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.web3.scph.mapper")
@EnableScheduling
public class Application {
    // 缓存最高区块（有可能超越DB最高区块）
    public static long currentBehindNum;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}