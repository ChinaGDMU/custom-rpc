package cn.damon;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ClassName ServerFrame
 * @Description
 * @Author Damon
 * @Date 2019/7/9 9:19
 * @Email zdmsjyx@163.com
 * @Version 1.0
 */
public class RpcFrame {

    private final ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private final Map<String, Class<?>> serviceHolder = new HashMap<>();

    private final int port;

    public RpcFrame(int port) {
        this.port = port;
    }

    public void registerService(Class<?> serviceInterface, Class<?> impl) {
        Socket socket = null;
        ObjectOutputStream o = null;
        ObjectInputStream i = null;
        try {
            socket = new Socket();
            InetSocketAddress addr = new InetSocketAddress("127.0.0.1", 2181);
            socket.connect(addr);
            o = new ObjectOutputStream(socket.getOutputStream());

            o.writeBoolean(true);
            o.writeUTF(serviceInterface.getName());
            o.writeUTF("127.0.0.1");
            o.writeInt(port);
            o.flush();

            i = new ObjectInputStream(socket.getInputStream());

            boolean reg = i.readBoolean();
            if (reg) {
                System.out.println("成功注册服务:" + serviceInterface.getName());
                serviceHolder.put(serviceInterface.getName(), impl);
            } else {
                System.out.println("注册服务失败");
            }

        } catch (Exception e) {
            System.out.println("服务中心还没有起来...");
        } finally {
            try {
                if (o != null) {
                    o.close();
                }
                if (i != null) {
                    i.close();
                }
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void startServer() {
        ServerSocket server = null;
        try {
            server = new ServerSocket();
            server.bind(new InetSocketAddress(port));
            while(true){
               pool.execute(new ServerTask(server.accept()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (server != null) {
                    server.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public class ServerTask implements Runnable {

        private Socket socket;

        public ServerTask(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (ObjectInputStream i = new ObjectInputStream(socket.getInputStream());
                 ObjectOutputStream o = new ObjectOutputStream(socket.getOutputStream())) {

                //服务名称
                String serviceNames = i.readUTF();
                //方法名称
                String methodName = i.readUTF();
                //参数类型
                Class<?>[] paramTypes = (Class<?>[]) i.readObject();
                //参数
                Object[] params = (Object[]) i.readObject();
                //调用服务
                Class<?> serviceImpl = serviceHolder.get(serviceNames);
                Method method = serviceImpl.getMethod(methodName, paramTypes);
                Object result = method.invoke(serviceImpl.newInstance(), params);

                o.writeObject(result);
                o.flush();

            } catch (Exception e) {
                try {
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }
        }
    }
}
