package com.example.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.seckill.entity.Goods;
import com.example.seckill.entity.Order;
import com.example.seckill.entity.SeckillGoods;
import com.example.seckill.entity.User;
import com.example.seckill.vo.RespBean;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Castle
 * @since 2021-08-05
 */
public interface ISeckillGoodsService extends IService<SeckillGoods> {

    /**
     * 秒杀
     *
     * @param goods
     * @param user
     * @return order
     */
    RespBean secKill(Goods goods, User user);
}
