package com.example.seckill.controller;


import cn.hutool.core.util.IdUtil;
import com.example.seckill.config.AccessLimit;
import com.example.seckill.entity.User;
import com.example.seckill.service.IGoodsService;
import com.example.seckill.vo.GoodsVo;
import com.example.seckill.vo.RespBean;
import com.wf.captcha.ArithmeticCaptcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Castle
 * @since 2021-08-05
 */
@RestController
@RequestMapping("/goods")
@Slf4j
public class GoodsController {

    @Resource
    private IGoodsService goodsService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @RequestMapping(value = "toList")
    public RespBean list() {
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        return RespBean.success(goodsList);
    }

    @AccessLimit(second = 30, maxCount = 5, needLogin = true)
    @GetMapping("details/{id}")
    public RespBean goodsDetail(@PathVariable("id") Long id, User user) {
        GoodsVo goods = goodsService.getGoodsVo(id);
        int secKillStatus = 0;
        int remainSeconds = 0;
        Date nowDate = new Date();
        Date startDate = goods.getStartDate();
        Date endDate = goods.getEndDate();
        if (nowDate.before(startDate)) {
            remainSeconds = (int) ((startDate.getTime() - nowDate.getTime()) / 1000);
        }
        if (endDate.before(nowDate)) {
            secKillStatus = 2;
            remainSeconds = -1;
        } else if (startDate.before(nowDate)) {
            secKillStatus = 1;
        }

        // 算术类型
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(130, 48);
        String key = "captcha:" + IdUtil.simpleUUID();
        redisTemplate.opsForValue().set(key, captcha.text(), 60, TimeUnit.SECONDS);
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("goods", goods);
        map.put("secKillStatus", secKillStatus);
        map.put("remainSeconds", remainSeconds);
        map.put("key", key);
        map.put("captcha", captcha.toBase64());
        return RespBean.success(map);
    }
}
