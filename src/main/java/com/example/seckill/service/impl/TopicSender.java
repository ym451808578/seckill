package com.example.seckill.service.impl;

import cn.hutool.json.JSONUtil;
import com.example.seckill.vo.SecKillVo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 消息队列发送
 *
 * @author Castle
 * @Date 2021/8/10 13:23
 */
@Component
public class TopicSender {
    @Resource
    private RabbitTemplate rabbitTemplate;

    public void send(SecKillVo secKillVo) {
        rabbitTemplate.convertAndSend("exchange_secKill", "topic_secKill", JSONUtil.toJsonStr(secKillVo));
    }
}
