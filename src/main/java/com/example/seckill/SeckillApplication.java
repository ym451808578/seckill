package com.example.seckill;

import cn.hutool.crypto.digest.DigestUtil;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @author castle
 */
@SpringBootApplication
@MapperScan("com.example.seckill.mapper")
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 30 * 60 * 1000)
@Slf4j
public class SeckillApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeckillApplication.class, args);
    }

}
