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
import org.springframework.data.redis.core.RedisTemplate;
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
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public RespBean doLogin(LoginParam loginParam, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("nickname", loginParam.getMobile());
        User user = userMapper.selectOne(wrapper);
        if (!user.getPassword().equals(DigestUtil.md5Hex(loginParam.getPassword() + user.getSalt()))) {
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        user.setPassword(null);
        String uuid = IdUtil.simpleUUID();
        session.setAttribute("user", user);
        //使用cookie登录，便于jmeter压力测试
        redisTemplate.opsForValue().set("sessionId:" + uuid, user);
        Cookie cookie = new Cookie("sessionId", uuid);
        cookie.setPath("/");
        response.addCookie(cookie);
        return RespBean.success(user);
    }
}
