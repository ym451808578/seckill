package com.example.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.seckill.entity.Goods;
import com.example.seckill.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Castle
 * @since 2021-08-05
 */
public interface GoodsMapper extends BaseMapper<Goods> {

    /**
     * 查询秒杀商品
     *
     * @return
     */
    List<GoodsVo> selectGoodsVoList();

    /**
     * 根据id查询秒杀商品
     *
     * @param id
     * @return
     */
    GoodsVo selectGoodsVo(Long id);
}
