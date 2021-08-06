package com.example.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.seckill.entity.*;
import com.example.seckill.mapper.GoodsMapper;
import com.example.seckill.mapper.OrderMapper;
import com.example.seckill.mapper.SeckillGoodsMapper;
import com.example.seckill.mapper.SeckillOrderMapper;
import com.example.seckill.service.ISeckillGoodsService;
import lombok.extern.slf4j.Slf4j;
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
    private GoodsMapper goodsMapper;
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private SeckillOrderMapper seckillOrderMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order secKill(Goods goods, User user) {
        QueryWrapper<SeckillGoods> wrapper = new QueryWrapper();
        wrapper.eq("goods_id", goods.getId());
        //秒杀商品库存-1
        SeckillGoods seckillGoods =seckillGoodsMapper.selectList(wrapper).get(0);
        log.info(seckillGoods.toString());
        seckillGoods.setStockCount(seckillGoods.getStockCount() - 1);
        seckillGoodsMapper.updateById(seckillGoods);

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

        return order;
    }
}
