package cn.damon.vo;

import java.io.Serializable;

/**
 * @ClassName RegisterVo
 * @Description
 * @Author Damon
 * @Date 2019/7/8 23:00
 * @Email zdmsjyx@163.com
 * @Version 1.0
 */
public class RegisterVo implements Serializable {
    /**
     * 序列化id
     */
    private static final long serialVersionUID = 4125096758372084309L;

    private String addr;
    private int port;

    public RegisterVo(String addr, int port) {
        this.addr = addr;
        this.port = port;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
