package com.example.seckill;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author castle
 */
@SpringBootApplication
@MapperScan("com.example.seckill.mapper")
@EnableCaching
@Slf4j
public class SeckillApplication {


    public static void main(String[] args) {

        SpringApplication.run(SeckillApplication.class, args);

    }
}
