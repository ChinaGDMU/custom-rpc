package cn.damon.rpc;

import cn.damon.RpcFrame;
import cn.damon.service.OrderService;
import cn.damon.service.impl.OrderServiceImpl;

/**
 * @ClassName RegOrderService
 * @Description
 * @Author Damon
 * @Date 2019/7/9 10:16
 * @Email zdmsjyx@163.com
 * @Version 1.0
 */
public class RegOrderService {
    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    int port = 9092;
                    RpcFrame r = new RpcFrame(port);
                    r.registerService(OrderService.class, OrderServiceImpl.class);
                    System.out.println("订单服务启动在:"+port);
                    r.startServer();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
