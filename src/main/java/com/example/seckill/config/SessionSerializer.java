package com.example.seckill.config;

import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

/**
 * SpringSession Redis序列化
 * *注：bean的名称必须为springSessionDefaultRedisSerializer
 *
 * @see org.springframework.session.data.redis.config.annotation.web.http.RedisHttpSessionConfiguration
 * * @author Castle
 * * @Date 2021/8/9 9:28
 */
@Component("springSessionDefaultRedisSerializer")
public class SessionSerializer extends GenericJackson2JsonRedisSerializer {
}
