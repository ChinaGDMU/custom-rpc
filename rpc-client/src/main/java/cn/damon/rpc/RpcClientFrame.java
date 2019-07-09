package cn.damon.rpc;

import cn.damon.vo.RegisterVo;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Executors;

/**
 * @ClassName RpcClientFrame
 * @Description
 * @Author Damon
 * @Date 2019/7/9 10:35
 * @Email zdmsjyx@163.com
 * @Version 1.0
 */
public class RpcClientFrame {

    public static  <T> T getRemoteClass(Class<?> serviceInterface){
        InetSocketAddress addr = new InetSocketAddress("127.0.0.1",2181);
        return (T)Proxy.newProxyInstance(serviceInterface.getClassLoader()
                ,new Class<?>[]{serviceInterface}
                ,new DynProxy(serviceInterface,addr));
    }

    //动态代理类
    private static class DynProxy implements InvocationHandler {

        private final Class<?> serviceInterface;
        private final InetSocketAddress addr;
        private RegisterVo[] serviceArray;/*远程服务在本地的缓存列表*/

        public DynProxy(Class<?> serviceInterface, InetSocketAddress addr) {
            this.serviceInterface = serviceInterface;
            this.addr = addr;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args)
                throws Throwable {
            Socket client = null;
            ObjectOutputStream o = null;
            ObjectInputStream i = null;

            //缓存列表为空的时候
        /*检索远程服务并填充本地的缓存列表*/
            if(serviceArray==null){
                try{
                    client = new Socket();
                    client.connect(addr);
                    o = new ObjectOutputStream(client.getOutputStream());
                    o.writeBoolean(false);
                    o.writeUTF(serviceInterface.getName());
                    o.flush();
                    i = new ObjectInputStream(client.getInputStream());
                    Set<RegisterVo> result = (Set<RegisterVo>)i.readObject();
                    serviceArray = new RegisterVo[result.size()];
                    result.toArray(serviceArray);
                }catch (Exception e){
                    System.out.println("注册中心尚未启动....");
                    return proxy;
                }
                finally {
                    if (client!=null) client.close();
                    if (o!=null) o.close();
                    if (i!=null) i.close();
                }

            }

            //缓存列表不为空的时候
            Random r = new Random();
            int index = r.nextInt(serviceArray.length);
            InetSocketAddress address = new InetSocketAddress(serviceArray[index].getAddr(),serviceArray[index].getPort());
            Socket s = null;
            ObjectInputStream is = null;
            ObjectOutputStream os = null;
            try{
                s = new Socket();
                s.connect(address);
                os = new ObjectOutputStream(s.getOutputStream());
                //服务名称
                os.writeUTF(serviceInterface.getName());
                //方法
                os.writeUTF(method.getName());
                //参数类型
                os.writeObject(method.getParameterTypes());
                //参数
                os.writeObject(args);
                os.flush();
                is = new ObjectInputStream(s.getInputStream());
                return is.readObject();
            }finally {
                if(is != null) is.close();
                if(os != null) os.close();
                if(s != null) s.close();
            }
        }
    }
}
