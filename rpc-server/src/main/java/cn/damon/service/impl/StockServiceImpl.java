package cn.damon.service.impl;

import cn.damon.service.StockService;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName StockService
 * @Description 库存服务
 * @Author Damon
 * @Date 2019/7/9 9:22
 * @Email zdmsjyx@163.com
 * @Version 1.0
 */
public class StockServiceImpl implements StockService {

    private final AtomicInteger stock = new AtomicInteger(1000);

    @Override
    public void addStock(int num) {
        stock.set(stock.get() + num);
        System.out.println("增加库存:"+num+"当前库存:"+stock.get());
    }

    @Override
    public void removeStock(int num) {
        if(stock.get() >= num){
            stock.set(stock.get()-num);
            System.out.println("扣除库存:"+num);
            System.out.println("剩余库存:"+stock.get());
        }else{
            System.out.println("想要扣去库存:"+num);
            System.out.println("但是库存不足");
        }
    }
}
