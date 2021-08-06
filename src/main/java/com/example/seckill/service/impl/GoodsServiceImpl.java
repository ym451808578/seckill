package com.example.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.seckill.entity.Goods;
import com.example.seckill.mapper.GoodsMapper;
import com.example.seckill.service.IGoodsService;
import com.example.seckill.vo.GoodsVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Castle
 * @since 2021-08-05
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {

    @Resource
    private GoodsMapper goodsMapper;
    @Override
    public List<GoodsVo> listGoodsVo() {
        return goodsMapper.selectGoodsVoList();
    }

    @Override
    public GoodsVo getGoodsVo(Long id) {
        return goodsMapper.selectGoodsVo(id);
    }
}
