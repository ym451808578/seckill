package com.example.seckill.vo;

import com.example.seckill.validate.IsMobile;
import lombok.Data;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;

/**
 * @author Castle
 * @Date 2021/8/4 7:05 上午
 */
@Data
public class LoginParam {
    @NonNull
    @IsMobile
    private String mobile;
    @NonNull
    @Length(min = 32)
    private String password;
}
