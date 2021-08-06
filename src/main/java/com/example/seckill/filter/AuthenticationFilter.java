package com.example.seckill.filter;

import com.example.seckill.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author Castle
 * @Date 2021/8/4 3:04 下午
 */
@Component
@Slf4j
public class AuthenticationFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        try {
            Cookie cookie = Arrays.stream(request.getCookies()).filter(c -> c.getName().equals("sessionId")).findFirst().get();
            String uuid = cookie.getValue();
            HttpSession session = request.getSession(false);
            User user = (User) session.getAttribute(uuid);
            if (user == null) {
                response.sendRedirect("/user/toLogin");
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("请登陆");
            response.sendRedirect("/user/toLogin");
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
