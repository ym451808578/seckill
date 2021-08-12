package com.example.seckill.controller;

import cn.hutool.core.util.IdUtil;
import com.example.seckill.entity.User;
import com.example.seckill.service.IUserService;
import com.example.seckill.vo.LoginParam;
import com.example.seckill.vo.RespBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

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

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping("toLogin")
    public String toLogin() {
        return "login";
    }

    @PostMapping("doLogin")
    @ResponseBody
    public RespBean doLogin(@Valid LoginParam loginParam, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        return userService.doLogin(loginParam, session, request, response);
    }

    @GetMapping("create")
    @ResponseBody
    public RespBean createUser() {
        log.info("create user");
        List<User> users = new ArrayList<>(5000);
        //生成用户
        for (int i = 0; i < 5000; i++) {
            User user = new User();
            user.setId(10000L + i);
            user.setNickname("1381234" + i);
            users.add(user);
        }
        log.info("create user");

        File file = new File("C:\\Users\\45180\\Desktop\\config.txt");
        if (file.exists()) {
            file.delete();
        }
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(file, "rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            raf.seek(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            String uuid = IdUtil.simpleUUID();
            String row = user.getId() + "," + uuid;
            redisTemplate.opsForValue().set("sessionId:" + uuid, user);
            try {
                raf.seek(raf.length());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                raf.write(row.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                raf.write("\r\n".getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("write to file : " + user.getId());
        }
        try {
            raf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("over");
        return RespBean.success();
    }
}
