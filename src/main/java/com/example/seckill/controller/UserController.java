package com.example.seckill.controller;

import com.example.seckill.service.IUserService;
import com.example.seckill.vo.LoginParam;
import com.example.seckill.vo.RespBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Castle
 * @since 2021-08-03
 */
@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private IUserService userService;

    @GetMapping("toLogin")
    public String toLogin() {
        return "login";
    }

    @PostMapping("doLogin")
    @ResponseBody
    public RespBean doLogin(@Valid LoginParam loginParam, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        log.info("controller");
        log.info(request.getContextPath());
        return userService.doLogin(loginParam, session, request,response);
    }
}
