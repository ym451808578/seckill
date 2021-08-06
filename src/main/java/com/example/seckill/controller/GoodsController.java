package com.example.seckill.controller;


import com.example.seckill.entity.User;
import com.example.seckill.service.IGoodsService;
import com.example.seckill.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Castle
 * @since 2021-08-05
 */
@Controller
@RequestMapping("/goods")
@Slf4j
public class GoodsController {

    @Resource
    private IGoodsService goodsService;

    @RequestMapping("toList")
    public String list(Model model) {
        log.info("goods/toList");
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        log.info(goodsList.toString());
        model.addAttribute("goodsList", goodsList);
        return "goodsList";
    }

    @GetMapping("goodsDetail/{id}")
    public String goodsDetail(@PathVariable("id") Long id, Model model, HttpServletRequest request) {
        Cookie cookie = Arrays.stream(request.getCookies()).filter(c -> c.getName().equals("sessionId")).findFirst().get();
        String uuid = cookie.getValue();
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute(uuid);
        user.setPassword("");
        GoodsVo goods = goodsService.getGoodsVo(id);
        int secKillStatus = 0;
        int remainSeconds=0;
        Date nowDate = new Date();
        Date startDate = goods.getStartDate();
        Date endDate = goods.getEndDate();
        if (nowDate.before(startDate)){
            remainSeconds= (int) ((startDate.getTime()-nowDate.getTime())/1000);
        }
        if (endDate.before(nowDate)) {
            secKillStatus = 2;
            remainSeconds=-1;
        } else if (startDate.before(nowDate)) {
            secKillStatus = 1;
        }
        log.info("secKillStatus:{}", secKillStatus);
        model.addAttribute("user", user);
        model.addAttribute("goods", goods);
        model.addAttribute("secKillStatus", secKillStatus);
        model.addAttribute("remainSeconds",remainSeconds);
        return "goodsDetail";
    }
}
