package com.example.seckill.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Castle
 * @Date 2021/8/10 13:08
 */
@Configuration
public class RabbitmqConfig {

    /**
     * 定义topic模式的交换器
     * exchange 交换器 名称  exchange_secKill
     * 队列名称  queue_secKill
     * <p>
     * routing key 定义 topic_secKill
     */
    @Bean
    public TopicExchange exchangeTopic() {
        return new TopicExchange("exchange_secKill");
    }

    @Bean
    public Queue queueTopic() {
        return new Queue("queue_secKill");
    }

    @Bean
    public Binding bindingTopic() {
        return BindingBuilder.bind(queueTopic()).to(exchangeTopic()).with("topic_secKill");
    }
}
