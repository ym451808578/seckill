package com.example.seckill.vo;

import com.example.seckill.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Castle
 * @Date 2021/8/10 13:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecKillVo implements Serializable {
    private User user;
    private GoodsVo goodsVo;
}
