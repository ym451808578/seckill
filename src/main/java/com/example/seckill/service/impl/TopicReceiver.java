package com.example.seckill.service.impl;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.example.seckill.service.ISeckillGoodsService;
import com.example.seckill.vo.GoodsVo;
import com.example.seckill.vo.SecKillVo;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author Castle
 * @Date 2021/8/10 13:27
 */
@Slf4j
@Component
public class TopicReceiver {
    @Resource
    private ISeckillGoodsService seckillGoodsService;

    @RabbitListener(queues = "queue_secKill")
    public void processSecKill(Message message, Channel channel) throws IOException {
        // 手动应答
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
        SecKillVo secKillVo = JSONUtil.toBean(new String(message.getBody()), SecKillVo.class);
        //秒杀
        seckillGoodsService.secKill(secKillVo.getGoodsVo(), secKillVo.getUser());
    }
}

