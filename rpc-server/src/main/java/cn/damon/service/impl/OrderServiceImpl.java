package cn.damon.service.impl;

import cn.damon.service.OrderService;

import java.util.UUID;

/**
 * @ClassName OrderServiceImpl
 * @Description
 * @Author Damon
 * @Date 2019/7/9 9:28
 * @Email zdmsjyx@163.com
 * @Version 1.0
 */
public class OrderServiceImpl implements OrderService {
    @Override
    public String makeOrder() {
        return "生成订单，订单号:"+ UUID.randomUUID().toString();
    }
}
