import java.lang.reflect.Method;
public class $Proxy1 implements proxy.UserMgr{
    public $Proxy1(proxy.InvocationHandler2 h) {
        this.h = h;
    }
    proxy.InvocationHandler2 h;
    @Override
    public  void addUser() {
        try {
        Method md = proxy.UserMgr.class.getMethod("addUser");
        h.invoke(this, md);
        }catch(Exception e) {e.printStackTrace();}
    }
    @Override
    public  void delUser() {
        try {
        Method md = proxy.UserMgr.class.getMethod("delUser");
        h.invoke(this, md);
        }catch(Exception e) {e.printStackTrace();}
    }

}