import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class Hello {
    public static void main(String[] args) {
        MaoTai maoTai = new MaoTai();
        InvocationHandler handler = new ProxyHandler(maoTai);
        SellWine sellWineMaotai = (SellWine) Proxy.newProxyInstance(maoTai.getClass().getClassLoader(),
                // todo 注意这里的interface，不能使用classes
                maoTai.getClass().getInterfaces(), handler);
        sellWineMaotai.sellWine();

        Wuliangye wuliangye = new Wuliangye();
        SellWine sellWineWuLiangYe = (SellWine) Proxy.newProxyInstance(wuliangye.getClass().getClassLoader(),
                wuliangye.getClass().getInterfaces(), new ProxyHandler(wuliangye));
        sellWineWuLiangYe.sellWine();

        FuRongWang fuRongWang = new FuRongWang();
        SellCigarette sellCigaretteFuRongWang = (SellCigarette) Proxy.newProxyInstance(fuRongWang.getClass().getClassLoader(),
                fuRongWang.getClass().getInterfaces(), new ProxyHandler(fuRongWang));
        sellCigaretteFuRongWang.sellCigarette();

//        System.out.println(sellWineMaotai.getClass().getName());
//        System.out.println(sellWineWuLiangYe.getClass().getCanonicalName());
//        System.out.println(sellCigaretteFuRongWang.getClass().getCanonicalName());

        // todo 似乎动态代理只能适用于接口
//        Child child = new Child();
//        Parent parent = (Parent) Proxy.newProxyInstance(Parent.class.getClassLoader(),
//                Parent.class.getClasses(), new ProxyHandler(child));
//        parent.call();
    }

    static abstract class Parent {
        abstract void call();
    }

    static class Child extends Parent {
        @Override
        void call() {
            System.out.println("Child call()!");
        }
    }

    static class FuRongWang implements SellCigarette {
        @Override
        public void sellCigarette() {
            System.out.println("我卖的是芙蓉王");
        }
    }

    interface SellCigarette {
        void sellCigarette();
    }

    static class MaoTai implements SellWine {
        @Override
        public void sellWine() {
            System.out.println("我卖的是茅台酒");
        }
    }

    static class Wuliangye implements SellWine {

        @Override
        public void sellWine() {
            System.out.println("我卖的是五粮液");
        }
    }

    interface SellWine {
        void sellWine();
    }

    static class ProxyHandler implements InvocationHandler {
        private final Object mBrand;

        ProxyHandler(Object brand) {
            mBrand = brand;
        }

        @Override
        public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
            // o是自动生成的代理实例
//            System.out.println(o.getClass().getCanonicalName());
            System.out.println("销售开始    柜台是： " + this);
            Object invoke = method.invoke(mBrand, objects);
            System.out.println("销售结束");
            return invoke;
        }
    }
}
