package cn.damon.rpc;

import cn.damon.service.OrderService;
import cn.damon.service.StockService;

/**
 * @ClassName RpcClent
 * @Description
 * @Author Damon
 * @Date 2019/7/9 11:02
 * @Email zdmsjyx@163.com
 * @Version 1.0
 */
public class RpcClent {
    public static void main(String[] args) {
        OrderService orderService = RpcClientFrame.getRemoteClass(OrderService.class);
        System.out.println(orderService);
        String result = orderService.makeOrder();
        System.out.println("调用订单结果:"+result);

        StockService stockService = RpcClientFrame.getRemoteClass(StockService.class);
        stockService.addStock(1000);
        stockService.removeStock(200);
    }
}
