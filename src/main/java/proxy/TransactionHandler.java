package proxy;

import java.lang.reflect.Method;

public class TransactionHandler implements InvocationHandler2 {

    private Object target;

    public TransactionHandler(Object target) {
        super();
        this.target = target;
    }

    @Override
    public void invoke(Object o, Method m) {
        String tag = toString();
        System.out.println("开启事务....." + tag);
        try {
            m.invoke(target);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("提交事务....." + tag);
    }

}

