package com.example.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.seckill.entity.User;
import com.example.seckill.vo.LoginParam;
import com.example.seckill.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Castle
 * @since 2021-08-03
 */
public interface IUserService extends IService<User> {

    /**
     * 用户登陆
     *
     * @param loginParam
     * @param  session
     * @return
     */
    RespBean doLogin(LoginParam loginParam, HttpSession session, HttpServletRequest request, HttpServletResponse response);

}
