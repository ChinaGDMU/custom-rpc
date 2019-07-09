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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RegisterVo that = (RegisterVo) o;

        if (port != that.port) return false;
        return addr != null ? addr.equals(that.addr) : that.addr == null;
    }

    @Override
    public int hashCode() {
        int result = addr != null ? addr.hashCode() : 0;
        result = 31 * result + port;
        return result;
    }
}
