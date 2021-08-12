package com.example.seckill.controller;


import com.example.seckill.config.AccessLimit;
import com.example.seckill.config.UserContext;
import com.example.seckill.entity.Order;
import com.example.seckill.entity.User;
import com.example.seckill.service.IGoodsService;
import com.example.seckill.service.impl.TopicSender;
import com.example.seckill.vo.GoodsVo;
import com.example.seckill.vo.RespBean;
import com.example.seckill.vo.RespBeanEnum;
import com.example.seckill.vo.SecKillVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Castle
 * @since 2021-08-05
 */
@RestController
@RequestMapping("/seckill")
@Slf4j
public class SeckillGoodsController {

    @Resource
    private IGoodsService goodsService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private TopicSender topicSender;

    /**
     * 初始化秒杀库存到redis中
     *
     * @return
     */
    @GetMapping("prepare")
    public RespBean prepareSecKill() {
        List<GoodsVo> goodsVos = goodsService.listGoodsVo();
        for (GoodsVo goodsVo : goodsVos) {
            if (goodsVo.getStockCount() > 0) {
                redisTemplate.opsForValue().set("secKillGoodsNum:" + goodsVo.getId(), goodsVo.getStockCount());
            }
        }
        return RespBean.success("设置成功");
    }

    /**
     * 获取本用户随机的秒杀地址
     *
     * @param user
     * @return
     */
    @GetMapping("path")
    public RespBean createPath(User user) {
        if (user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        return RespBean.success(goodsService.createPath(user));
    }

    @AccessLimit(maxCount = 5, second = 30, needLogin = true)
    @PostMapping("{path}/doSeckill")
    public RespBean doSeckill(@PathVariable("path") String path,
                              @RequestParam("goodsId") Long goodsId,
                              @RequestParam("key") String key,
                              @RequestParam("captcha") String captcha,
                              User user) {
        if (user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        //检查验证码
        String oCaptcha = (String) redisTemplate.opsForValue().get(key);
        log.info("captcha:{}", captcha);
        log.info("oCaptcha:{}", oCaptcha);
        if (!oCaptcha.equals(captcha)) {
            return RespBean.error(RespBeanEnum.CAPTCHA_ERROR);
        }
        //检查path
        if (!goodsService.checkPath(user, path)) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        int goodsNum = (int) redisTemplate.opsForValue().get("secKillGoodsNum:" + goodsId);
        log.info("goodsNum:{}", goodsNum);
        //如果数量小于1了，表示秒杀结束
        if (goodsNum < 1) {
            return RespBean.error(RespBeanEnum.GOODS_STOCK_ZERO);
        }
        //判断商品是否存在
        GoodsVo goodsVo = goodsService.getGoodsVo(goodsId);
        if (goodsVo == null) {
            return RespBean.error(RespBeanEnum.GOODS_NOT_EXIST);
        }
        //判断同一用户是否重复下单
        Boolean hasKey = redisTemplate.hasKey("seckill:order:" + goodsId + ":" + user.getId());
        if (hasKey) {
            return RespBean.error(RespBeanEnum.SEC_KILL_REPEAT);
        }
        //库存减1，交给消息队列处理
        Long decrement = redisTemplate.opsForValue().decrement("secKillGoodsNum:" + goodsId);
        log.info("decrement:{}", decrement);
        if (decrement > -1) {
            topicSender.send(new SecKillVo(user, goodsVo));
        }
        return RespBean.success();
    }

    @GetMapping("result/{goodsId}")
    public RespBean getResult(@PathVariable("goodsId") Long goodsId, User user) {
        if (user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        String key = "seckill:order:" + goodsId + ":" + user.getId();
        log.info("key:{}", key);
        Long orderId = 0L;
        //获取秒杀订单
        Boolean aBoolean = redisTemplate.hasKey("seckill:order:" + goodsId + ":" + user.getId());
        System.out.println(aBoolean);
        if (aBoolean) {
            log.info(redisTemplate.opsForValue().get("seckill:order:" + goodsId + ":" + user.getId()).toString());
            orderId = Long.parseLong(redisTemplate.opsForValue().get("seckill:order:" + goodsId + ":" + user.getId()).toString());
        }
        log.info("orderId:{}", orderId);
        return RespBean.success(orderId);
    }
}
