package com.example.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.seckill.entity.Goods;
import com.example.seckill.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Castle
 * @since 2021-08-05
 */
public interface IGoodsService extends IService<Goods> {

    /**
     * 查询秒杀商品列表
     *
     * @return
     */
    List<GoodsVo> listGoodsVo();

    /**
     * 根据商品id获取秒杀商品
     *
     * @param id
     * @return
     */
    GoodsVo getGoodsVo(Long id);
}
