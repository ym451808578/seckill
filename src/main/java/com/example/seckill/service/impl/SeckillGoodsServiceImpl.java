package com.example.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.seckill.entity.*;
import com.example.seckill.mapper.OrderMapper;
import com.example.seckill.mapper.SeckillGoodsMapper;
import com.example.seckill.mapper.SeckillOrderMapper;
import com.example.seckill.service.ISeckillGoodsService;
import com.example.seckill.vo.RespBean;
import com.example.seckill.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Castle
 * @since 2021-08-05
 */
@Service
@Slf4j
public class SeckillGoodsServiceImpl extends ServiceImpl<SeckillGoodsMapper, SeckillGoods> implements ISeckillGoodsService {

    @Resource
    private SeckillGoodsMapper seckillGoodsMapper;
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private SeckillOrderMapper seckillOrderMapper;
    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 秒杀操作
     *
     * @param goods
     * @param user
     * @return
     * @Caching(evict = {
     * @CacheEvict(value = CACHE_OBJECT, key = CACHE_LIST_KEY),   //删除Goods List集合缓存
     * @CacheEvict(value = CACHE_OBJECT, key = "#id")  //删除单条Goods记录缓存
     * })
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RespBean secKill(Goods goods, User user) {
        SeckillGoods seckillGoods = seckillGoodsMapper.selectForUpdate(goods.getId());
        if (seckillGoods == null) {
            return RespBean.error(RespBeanEnum.GOODS_NOT_EXIST);
        }
        //秒杀商品库存-1
        seckillGoods.setStockCount(seckillGoods.getStockCount() - 1);
        seckillGoodsMapper.update(seckillGoods, new UpdateWrapper<SeckillGoods>().
                set("stock_count", seckillGoods.getStockCount()).
                eq("id", seckillGoods.getId()).
                gt("stock_count", 0));

        //生成商品订单
        Order order = new Order();
        order.setUserId(user.getId());
        order.setGoodsId(seckillGoods.getGoodsId());
        order.setDelivertyAddrId(0L);
        order.setGoodsName(goods.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(seckillGoods.getSeckillPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateDate(new Date());
        orderMapper.insert(order);

        //生成秒杀订单
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setGoodsId(goods.getId());
        seckillOrder.setOrderId(order.getId());
        seckillOrder.setUserId(user.getId());
        seckillOrderMapper.insert(seckillOrder);
        //将秒杀订单放入redis，防止同一用户重复下单
        redisTemplate.opsForValue().set("seckill:order:" + goods.getId() + ":" + user.getId(), seckillOrder.getOrderId());
        return RespBean.success();
    }
}
