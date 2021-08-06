package com.example.seckill.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author Castle
 * @since 2021-08-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 商品ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    private String nickname;

    /**
     * MD5(MD5(password明文+固定salt)+salt)
     */
    private String password;

    private String salt;

    /**
     * 头像
     */
    private String head;

    /**
     * 注册时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date registerDate;

    /**
     * 最后一次登陆时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date lastLoginDate;

    /**
     * 登陆次数
     */
    private Integer loginCount;


}
