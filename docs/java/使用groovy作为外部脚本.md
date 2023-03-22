

引入依赖

```
<dependency>
    <groupId>org.codehaus.groovy</groupId>
    <artifactId>groovy-all</artifactId>
    <version>3.0.7</version>
    <type>pom</type>
</dependency>
```

使用evaluate调用groovy脚本【字符串】

```
public class Task1 {
    public static void main(String[] args) {
        GroovyShell groovyShell = new GroovyShell();
        groovyShell.evaluate("print 'hello world'");
    }
}
```

使用evaluate调用groovy脚本【文件】

```
public class Task2 {
    public static void main(String[] args) {
        GroovyShell groovyShell = new GroovyShell();
        Reader r = new InputStreamReader(Task2.class.getClassLoader().getResourceAsStream("Task2.groovy"));
        groovyShell.evaluate(r);
    }
}
```

```
def sayHello() {
    println 'Hello World.'
}
sayHello()
```

给groovy传递参数

```
public class Task3 {
    public static void main(String[] args) {
        Binding binding = new Binding();
        binding.setProperty("name", "groovy shell");

        GroovyShell groovyShell = new GroovyShell(binding);
        Reader r = new InputStreamReader(Task3.class.getClassLoader().getResourceAsStream("Task3Shell.groovy"));
        groovyShell.evaluate(r);
    }
}
```

```
package issues.groovy
def sayHello(name) {
    println 'Hello ' + name
}
sayHello(name)
```

使用GroovyClassLoader动态加载Groovy Class

```
public class Task4 {
    public static void main(String[] args) {
        CompilerConfiguration config = new CompilerConfiguration();
        config.setSourceEncoding("UTF-8");
        // 设置该GroovyClassLoader的父ClassLoader为当前线程的加载器(默认)
        GroovyClassLoader groovyClassLoader = new GroovyClassLoader(Thread.currentThread().getContextClassLoader(), config);

        Class clazz = null;
        try {
            clazz = groovyClassLoader.parseClass(new File("F:\\study\\2000-code\\java\\java-code\\issues\\issue-groovy\\src\\main\\resources\\Task4Class.groovy"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        GroovyObject groovyObject = null;
        try {
            groovyObject = (GroovyObject) clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        Object methodResult = groovyObject.invokeMethod("sayHello", new Object[] {"Arg0", "sex", 1});
        if (methodResult != null) {
            System.out.println(methodResult.toString());
        }

    }
}

```

```
package issues.groovy
class Task4Class {
    String sayHello(String arg0, String arg1, int arg2) {
        return "arg0: " + arg0 + ", arg1: " + arg1 + ", arg2: " + arg2;
    }
}
```

使用GroovyScriptEngin脚本引擎加载Groovy脚本

```
public class Task5 {
    public static void main(String[] args) throws Exception {
        Properties properties = new Properties();
        properties.put("x",1);
        properties.put("y",2);
        Binding binding = new Binding(properties);
        GroovyScriptEngine engine = new GroovyScriptEngine("F:\\study\\2000-code\\java\\java-code\\issues\\issue-groovy\\src\\main\\resources\\groovyTest");
        //        GroovyScriptEngine engine = new GroovyScriptEngine("src/main/resources/groovyTest");
        Object result1 = engine.run("shell1.groovy", binding);
        Object result2 = engine.run("shell2.groovy", binding);

        System.out.println(result1);
        System.out.println(result2);
    }
}
```

```
def add(int x,int y) {
    int r = x + y
    println(x + " + " + y + " = " + r)
    return r
}
add(x,y)
```

```
def add(int x,int y) {
    int r = x - y
    println(x + " - " + y + " = " + r)
    return r
}
add(x,y)
```

使用GroovyScriptEngine加载Groovy脚本，并且以Map作为参数，返回处理结果

```
public class Task6 {
    public static void main(String[] args) throws Exception {
        Map<String,Object> map = new HashMap<>();
        map.put("x",1);
        map.put("y",2);
        Properties properties = new Properties();
        properties.put("x",1);
        properties.put("y",2);
        properties.put("map",map);
        Binding binding = new Binding(properties);
        GroovyScriptEngine engine = new GroovyScriptEngine("F:\\study\\2000-code\\java\\java-code\\issues\\issue-groovy\\src\\main\\resources\\groovyTest");
        //        GroovyScriptEngine engine = new GroovyScriptEngine("src/main/resources/groovyTest");
//        Object result1 = engine.run("shell3.groovy", binding);
        String scriptName = "shell3.groovy";
//        Object result1 = engine.createScript(scriptName, binding).run();

//        Script script = InvokerHelper.createScript(engine.loadScriptByName(scriptName), binding);
        Class clazz = engine.loadScriptByName(scriptName);
        Script script = InvokerHelper.newScript(clazz, binding);

        Object result1 = script.run();

        System.out.println(result1);

        GroovyClassLoader loader = engine.getGroovyClassLoader();
        URLConnection conn = engine.getResourceConnection(scriptName);
        String path = conn.getURL().toExternalForm();
        String encoding = conn.getContentEncoding() != null ? conn.getContentEncoding() : engine.getConfig().getSourceEncoding();
        String content = IOGroovyMethods.getText(conn.getInputStream(), encoding);
        System.out.println("=========source==============");
        System.out.println(content);
    }
}
```

```
import com.alibaba.fastjson.JSON
import org.joda.time.DateTime

def m(Map map) {
    println(JSON.toJSONString(map))
    int x = map.get("x")
    int y = map.get("y")
    int r = x - y
    println(x + " - " + y + " = " + r)
    println(DateTime.now().toString("yyyyMMddHHmmss"))
    return r
}
m(map)
```

