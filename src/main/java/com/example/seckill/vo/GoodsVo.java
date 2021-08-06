package com.example.seckill.vo;

import com.example.seckill.entity.Goods;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Castle
 * @Date 2021/8/5 1:52 下午
 */
@Data
@ToString
public class GoodsVo extends Goods {

    /**
     * 秒杀价格
     */
    private BigDecimal seckillPrice;

    /**
     * 库存数量
     */
    private Integer stockCount;

    /**
     * 秒杀开始时间
     */
    private Date startDate;

    /**
     * 秒杀结束时间
     */
    private Date endDate;

}
