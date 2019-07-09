package cn.damon.rpc;

import cn.damon.RpcFrame;
import cn.damon.service.StockService;
import cn.damon.service.impl.StockServiceImpl;

import java.io.IOException;

/**
 * @ClassName RegStockService
 * @Description
 * @Author Damon
 * @Date 2019/7/9 10:05
 * @Email zdmsjyx@163.com
 * @Version 1.0
 */
public class RegStockService1 {
    public static void main(String[] args) throws IOException {


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int port = 9093;
                    RpcFrame rpcFrame = new RpcFrame(port);
                    rpcFrame.registerService(StockService.class, StockServiceImpl.class);
                    System.out.println("库存服务2启动在:"+port);
                    rpcFrame.startServer();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
