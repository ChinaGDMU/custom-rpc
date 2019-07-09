package cn.damon;

import cn.damon.vo.RegisterVo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName RegServer
 * @Description
 * @Author Damon
 * @Date 2019/7/8 22:59
 * @Email zdmsjyx@163.com
 * @Version 1.0
 */
public class RegFrame {

    private static volatile Map<String, Set<RegisterVo>> serviceHolder = new HashMap<>();

    public static synchronized void register(String servicePath, String ip, int port) {
        Set<RegisterVo> services = serviceHolder.get(servicePath);
        if (services == null) {
            Set<RegisterVo> set = new HashSet<>();
            set.add(new RegisterVo(ip, port));
            serviceHolder.put(servicePath, set);
            System.out.println(String.format("服务注册,serviceName:%s,ip:%s,port:%d", servicePath, ip, port));
        } else {
            services.add(new RegisterVo(ip, port));
            System.out.println(String.format("服务增加,serviceName:%s,ip:%s,port:%d", servicePath, ip, port));
        }
    }

    public static Set<RegisterVo> getService(String serviceName) {
        return serviceHolder.get(serviceName);
    }

    public static class ListeningRunner implements Runnable {

        private Socket socket;

        public ListeningRunner(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (ObjectInputStream ois =
                         new ObjectInputStream(socket.getInputStream());
                 ObjectOutputStream oos =
                         new ObjectOutputStream(socket.getOutputStream())) {
                //判断是注册服务还是调用服务
                Boolean reg = ois.readBoolean();
                if (reg) {
                    String serviceName = ois.readUTF();
                    String ip = ois.readUTF();
                    int port = ois.readInt();
                    //注册服务
                    register(serviceName, ip, port);
                    oos.writeBoolean(true);
                    oos.flush();
                } else {
                    //调用服务
                    String serviceName = ois.readUTF();
                    Set<RegisterVo> services = getService(serviceName);
                    oos.writeObject(services);
                    oos.flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }
    }

}
