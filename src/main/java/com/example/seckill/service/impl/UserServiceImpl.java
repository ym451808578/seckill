package com.example.seckill.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.seckill.entity.User;
import com.example.seckill.exception.GlobalException;
import com.example.seckill.mapper.UserMapper;
import com.example.seckill.service.IUserService;
import com.example.seckill.vo.LoginParam;
import com.example.seckill.vo.RespBean;
import com.example.seckill.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Castle
 * @since 2021-08-03
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public RespBean doLogin(LoginParam loginParam, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("nickname", loginParam.getMobile());
        User user = userMapper.selectOne(wrapper);
        if (!user.getPassword().equals(DigestUtil.md5Hex(loginParam.getPassword() + user.getSalt()))) {
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        String uuid = IdUtil.simpleUUID();
        log.info("uuid:{}", uuid);
        session.setAttribute(uuid, user);
        session.setAttribute("sessionId",uuid);
        Cookie cookie = new Cookie("sessionId", uuid);
        cookie.setMaxAge(-1);
        cookie.setDomain("localhost");
        cookie.setPath("/");
        response.addCookie(cookie);
        return RespBean.success(user);
    }
}
