package com.example.seckill.validate;

import cn.hutool.core.util.PhoneUtil;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Castle
 * @Date 2021/8/4 1:24 下午
 */
public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {

    private boolean required = false;

    @Override
    public void initialize(IsMobile isMobile) {
        required = isMobile.required();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (required) {
            return PhoneUtil.isMobile(s);
        } else {
            if (!StringUtils.hasText(s)) {
                return true;
            } else {
                return PhoneUtil.isMobile(s);
            }
        }
    }
}
