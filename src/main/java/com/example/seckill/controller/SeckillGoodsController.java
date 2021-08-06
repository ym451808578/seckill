package com.example.seckill.controller;


import com.example.seckill.entity.Goods;
import com.example.seckill.entity.Order;
import com.example.seckill.entity.User;
import com.example.seckill.service.IGoodsService;
import com.example.seckill.service.ISeckillGoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Castle
 * @since 2021-08-05
 */
@Controller
@RequestMapping("/seckill")
@Slf4j
public class SeckillGoodsController {

    @Resource
    private ISeckillGoodsService seckillGoodsService;
    @Resource
    private IGoodsService goodsService;

    @PostMapping("doSeckill")
    public String doSeckill(@RequestParam("goodsId") Long goodsId, HttpServletRequest request, Model model) {
        log.info("seckill:" + goodsId);
        Goods goods = goodsService.getById(goodsId);
        Cookie cookie = Arrays.stream(request.getCookies()).filter(c -> c.getName().equals("sessionId")).findFirst().get();
        String uuid = cookie.getValue();
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute(uuid);
        user.setPassword("");
        try {
            Order order = seckillGoodsService.secKill(goods, user);
            model.addAttribute("user", user);
            model.addAttribute("order", order);
            model.addAttribute("goods", goods);
        } catch (Exception e) {
            return "secKillFail";
        }
        return "OrderDetail";
    }

}
