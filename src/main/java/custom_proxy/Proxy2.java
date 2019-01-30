package custom_proxy;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * 模拟动态代理
 */
public class Proxy2 {
    public static void main(String[] args) throws Exception {
        UserMgr mgr = new UserMgrImpl();

        //为用户管理添加事务处理
        InvocationHandler2 h = new TransactionHandler(mgr);
        UserMgr u = (UserMgr) Proxy2.newProxyInstance(UserMgr.class, h);

        //为用户管理添加显示方法执行时间的功能
        InvocationHandler2 h2 = new TransactionHandler(u);
        u = (UserMgr) Proxy2.newProxyInstance(UserMgr.class, h2);

        u.addUser();
        System.out.println("\r\n==========华丽的分割线==========\r\n");
        u.delUser();
    }

    /**
     * @param infce 被代理类的接口
     * @param h     代理类
     * @throws Exception
     */
    private static Object newProxyInstance(Class infce, InvocationHandler2 h) throws Exception {
        String methodStr = "";
        String rt = "\r\n";

        //利用反射得到infce的所有方法，并重新组装
        Method[] methods = infce.getMethods();
        for (Method m : methods) {
            methodStr += "    @Override" + rt +
                    "    public  " + m.getReturnType() + " " + m.getName() + "() {" + rt +
                    "        try {" + rt +
                    "        Method md = " + infce.getName() + ".class.getMethod(\"" + m.getName() + "\");" + rt +
                    "        h.invoke(this, md);" + rt +
                    "        }catch(Exception e) {e.printStackTrace();}" + rt +
                    "    }" + rt;
        }

        //生成Java源文件
        String packageName = Proxy2.class.getPackage().getName();
//        System.out.println("packageName = " + packageName);
        String proxyName = getProxyName();
        String srcCode =
//                "package com.tgb.proxy;" + rt +
//                "package " + packageName + ";" + rt +
                "import java.lang.reflect.Method;" + rt +
                        "public class " + proxyName + " implements " + infce.getName() + "{" + rt +
                        "    public " + proxyName + "(" + packageName + ".InvocationHandler2 h) {" + rt +
                        "        this.h = h;" + rt +
                        "    }" + rt +
//                        "    com.tgb.proxy.custom_proxy.InvocationHandler2 h;" + rt +
                        "    " + packageName + ".InvocationHandler2 h;" + rt +
                        methodStr + rt +
                        "}";
        String path = new File("").getAbsolutePath() + "/src";
//        String prefix = path + "/main/java/" + packageName;
        String prefix = path + "/main/java/";
        String fileName = prefix + "/" + proxyName + ".java";
//                "d:/src/com/tgb/proxy/$Proxy1.java";
        new File(fileName).delete();
        new File(prefix + "/" + proxyName + ".class").delete();

        FileWriter fw = new FileWriter(fileName);
        fw.write(srcCode);
        fw.flush();
        fw.close();
        //将Java文件编译成class文件
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileMgr = compiler.getStandardFileManager(null, null, null);
        Iterable units = fileMgr.getJavaFileObjects(fileName);
        JavaCompiler.CompilationTask t = compiler.getTask(null, fileMgr, null, null, null, units);
        t.call();
        fileMgr.close();

        //加载到内存，并实例化
//        URL[] urls = new URL[]{new URL("file:/" + "d:/src/")};
        URL[] urls = new URL[]{new URL("file:/" + path)};
        URLClassLoader ul = new URLClassLoader(urls);
//        Class c = ul.loadClass("com.tgb.proxy.$Proxy1");
        Class c = ul.loadClass(proxyName + "");//这里不知道为什么找不到类

        Constructor ctr = c.getConstructor(InvocationHandler2.class);
        return ctr.newInstance(h);
    }

    private static String getProxyName() {
        return "$Proxy" + (sNum++);
    }

    private static int sNum = 0;
}
