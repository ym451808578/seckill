package com.example.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.seckill.entity.SeckillGoods;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Castle
 * @since 2021-08-05
 */
public interface SeckillGoodsMapper extends BaseMapper<SeckillGoods> {

    SeckillGoods selectForUpdate(Long id);
}
