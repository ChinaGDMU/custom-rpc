package cn.damon;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

/**
 * @ClassName RegServer
 * @Description
 * @Author Damon
 * @Date 2019/7/9 9:15
 * @Email zdmsjyx@163.com
 * @Version 1.0
 */
public class RegServer {
    public static void main(String[] args) throws IOException {
        ServerSocket s = new ServerSocket();
        final int port = 2181;
        s.bind(new InetSocketAddress(port));
        System.out.println("注册中心启动在:"+port);
        while (true) {
            new Thread(new RegFrame.ListeningRunner(s.accept())).start();
        }
    }
}
