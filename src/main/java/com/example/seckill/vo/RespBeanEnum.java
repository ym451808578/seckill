package com.example.seckill.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Castle
 * @Date 2021/8/4 11:59 上午
 */
@Getter
@ToString
@AllArgsConstructor
public enum RespBeanEnum {
    SUCCESS(200, "SUCCESS"),
    ERROR(500, "服务端异常"),
    //登录模块5002xx
    LOGIN_ERROR(500210, "用户名或密码不正确"),
    MOBILE_ERROR(500211, "手机号码格式不正确"),
    BIND_ERROR(500212, "参数校验异常"),
    MOBILE_NOT_EXIST(500213, "手机号码不存在"),
    PASSWORD_UPDATE_FAIL(500214, "密码更新失败"),
    SESSION_ERROR(500215, "用户不存在"),
    GOODS_NOT_EXIST(500310, "秒杀商品不存在"),
    GOODS_STOCK_ZERO(500311, "秒杀商品无库存"),
    SEC_KILL_REPEAT(500410, "秒杀重复下单"),
    CAPTCHA_ERROR(500510, "验证码错误"),
    ACCESS_LIMIT_REACHED(500511, "达到最大访问次数");

    private final Integer code;
    private final String message;
}
