package com.example.seckill.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.seckill.entity.Goods;
import com.example.seckill.entity.User;
import com.example.seckill.mapper.GoodsMapper;
import com.example.seckill.service.IGoodsService;
import com.example.seckill.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {
    /**
     * 缓存名称
     */
    public static final String CACHE_OBJECT = "goods";
    /**
     * 缓存list_Key
     */
    public static final String CACHE_LIST_KEY = "\"list\"";
    @Resource
    private GoodsMapper goodsMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    @Cacheable(value = CACHE_OBJECT, key = CACHE_LIST_KEY)
    public List<GoodsVo> listGoodsVo() {
        return goodsMapper.selectGoodsVoList();
    }

    @Override
    @Cacheable(value = CACHE_OBJECT, key = "#id")
    public GoodsVo getGoodsVo(Long id) {
        return goodsMapper.selectGoodsVo(id);
    }

    @Override
    public String createPath(User user) {
        String path = DigestUtil.md5Hex(IdUtil.simpleUUID());
        redisTemplate.opsForValue().set("secKillPath:" + user.getId(), path, 60, TimeUnit.SECONDS);
        return path;
    }

    @Override
    public boolean checkPath(User user, String path) {
        String qPath = (String) redisTemplate.opsForValue().get("secKillPath:" + user.getId());
        return qPath.equals(path);
    }
}
