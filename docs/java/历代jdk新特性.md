## JDK1.0

是纯解释运行,使用外挂JIT



## JDK1.1

- JDBC(Java DataBase Connectivity)
- 支持内部类
- RMI(Remote Method Invocation)
- 反射
-  Java Bean



## JDK1.2

- 集合框架
- JIT(Just In Time)编译器
- 对打包的Java文件进行数字签名
- JFC(Java Foundation Classes)包括Swing1.0,拖放和Java2D类库
- Java插件
- JDBC中引入可滚动结果集,BLOB,CLOB,批量更新和用户自定义类型
- Applet中添加声音支持



## JDK1.3

- Java Sound API
- Jar文件索引
- 对Java的各个方面都做了大量优化和增强



## JDK1.4

- XML处理
- Java打印服务
- Logging API
- Java Web Start
- JDBC 3.0API
- 断言
- Preferences API
- 链式异常处理
- 支持IPV6
- 支持正则表达式
- 引入Image I/O API



## JDK1.5

- 泛型
- 增强循环,可以使用迭代方式
- 自动装箱与自动拆箱
- 类型安全的枚举
- 可变参数
- ​	底层就是一个数组,根据参数个数不同,会创建不同长度的数组
- ​	在传递时可以直接传递数组(传递数组后就不能再传递其余可变参数)
- 静态引入
- 元数据(注解)
- Instrumentation



## JDK 1.6

- 支持脚本语言
- JDBC 4.0API
- Java Compiler API
- 可插拔注解
- 增加对Native PKI(Public Key Infrastructure),Java GSS(Generic Security Service)
- Kerberos和LDAP(Lightweight Directory Access Protocol)支持
- 继承Web Services



## JDK 1.7

1.switch支持字符串变量

2.泛型实例化类型自动推断

    ArrayList<String> list = new ArrayList<>();

3.新的整数字面表达方式 "ob"前缀 和 "_"连接符
    1.表示二进制字面值的前缀ob
        以下三个值相同

```
byte b1 = ob00100001;
        byte b2 = 0x21;
        byte b3 = 33;
```

_
    2.字面常量数字的下划线.用下划线连接整数提升其可读性,自身无含义,不可用在数字的起始和末尾
        Java编码语言对给数值型的字面值加下划线有严格的规定。如上所述，你只能在数字之间用下划线。你不能用把一个数字用下划线开头，或者已下划线结尾。这里有一些其它的不能在数值型字面值上用下划线的地方：
            在数字的开始或结尾
            对浮点型数字的小数点附件
            F或L下标的前面
            该数值型字面值是字符串类型的时候  
          

```
float pi1 = 3_.1415F; // 无效的; 不能在小数点之前有下划线
float pi2 = 3._1415F; // 无效的; 不能在小数点之后有下划线
long socialSecurityNumber1=999_99_9999_L;//无效的，不能在L下标之前加下划线
int a1 = _52; // 这是一个下划线开头的标识符，不是个数字
int a2 = 5_2; // 有效
int a3 = 52_; // 无效的，不能以下划线结尾
int a4 = 5_______2; // 有效的
int a5 = 0_x52; // 无效，不能在0x之间有下划线
int a6 = 0x_52; // 无效的，不能在数字开头有下划线
int a7 = 0x5_2; // 有效的 (16进制数字)
int a8 = 0x52_; // 无效的，不能以下划线结尾
int a9 = 0_52; // 有效的（8进制数）
int a10 = 05_2; // 有效的（8进制数）
int a11 = 052_; // 无效的，不能以下划线结尾
```

4.在单个catch块中捕获多个异常,以及用升级版的类型检查重新抛出异常
    catch代码块得到了升级,用以在单个catch块中处理多个异常,如果你要捕获多个异常并且它们包含相似的代码,使用这一特性会减少代码重复

```
 catch(IOException | SQLException | Exception ex) {}
```

​    这种情况下ex变量是final的
5.try-with-resources语句
​    try-with-resources语句是一个声明一个或多个资源的try语句,一个资源作为一个对象,必须在程序结束后关闭
​    try-with-resources语句确保在语句的最后每个资源都被关闭,任何实现了Java.lang.AutoCloseable和java.io.Closeable的对象都可以使用try-with-resource来实现异常处理和关闭资源
​    try-with-resource可以声明多个资源(声明语句之间用分好分割,最后一个可忽略分号)
​    try (流创建等需要关闭的资源打开操作,多个之间用;隔开) {操作} catch(Exception 这里可加catch可不加) {}
6.引入JavaNIO.2开发包
7.null值的自动处理



## JDK 1.8

- Lambda表达式 
  - Lambda允许把函数作为一个方法的参数(函数作为参数传递进方法中)
- 方法引用
  - 方法引用提供了非常有用的语法,可以直接引用已有的Java类或对象(实例)的方法或构造器.
  - 与lambda联合使用,方法引用可以使语言的构造更紧凑简洁,减少冗余代码
- 默认方法
  - 默认方法就是一个在接口里面有了一个实现的方法
- 新工具
  - 新的编译工具 如:Nashorn引擎 js 类依赖分析器jdeps
- Stream API
  - 新添加的Stream API(java.utils.stream)把真正的函数式编程风格引入到Java中
- Data Time API
  - 加强对日期与时间的处理
- Optional类
  - Optional类已经成为Java8类库的一部分,可用来解决空指针异常
- Nashorn,JavaScript引擎
  - Java8提供了一个新的Nashorn JavaScript引擎,它允许我们在JVM上运行特定的JavaScript应用



## JDK 1.9

模块系统

- 模块是一个包的容器,Java9最大的变化之一是引入了模块系统(Jigsaw项目)

REPL(JShell)

- 交互式编程环境
- 在cmd中输入JShell 可以帮你自动创建类
- 以及运行一些简单的方法

HTTP2客户端

- HTTP/2标准是HTTP协议的最新版本,新的HTTPClient API支持WebSocket和HTTP2流以及服务器推送特性

改进的Javadoc

- Javadoc现在支持在API文档中的进行搜索,另外Javadoc的输出现在符合兼容HTML5标准

 多版本兼容Jar包

- 多版本兼容Jar功能能让你创建仅在特定版本的Java环境中运行库程序时选择使用的class版本

集合工厂方法

- List,Set和Map接口中,新的静态工厂方法可以创建这些集合的不可变实例

私有接口方法

- 在接口中使用private私有方法.我们可以使用private访问修饰符在接口中编写私有方法

进程API

- 改进的API来控制和管理操作系统进程.改进java.lang.ProcessHandle及其嵌套接口info来让开发者逃离时常因为要获取一个本地进程的PID而不得不适用本地代码的窘境

改进的Stream API

- 改进的Stream API添加了一些便利的方法,使流处理更容易,并使用收集器编写复杂的查询

改进 try-with-resources

- 如果你已经有一个资源是final或等效于final变量,您可以在try-with-resources语句中使用该变量,而无需再try-with-resources语句中声明一个新变量

改进的弃用注解@Deprecated

- 可以标记Java API的状态 可以表示被标记的API将会被移除,或者已被破坏

改进钻石操作符(Diamond Operator)

- 匿名类可以使用钻石操作符(Diamond Operator)

改进Optional类

- java.util.Optional添加了很多新的有用方法,Optional可以直接转为stream

多分辨率图像API

- 定义多分辨率图像API,开发者可以很容易的操作和展示不同分辨率的图像了

改进的CompletableFuture API

- CompletableFuture类的异步机制可以在ProcessHandle.onExit方法退出时执行操作

轻量级的JSON API

- 内置了一个轻量级的JSON API

 响应式流(Reactive Streams)API

- Java9中引入了新的响应式流API来支持Java9中的响应式编程

模块化

- 可定制JRE:更小的运行时镜像
- 更确定的模块依赖关系:避免Jar Hell问题
- 与 OSGI 的比较

模块的定义:

- 模块一个命名的,自我描述的代码和数据的集合
- ​	模块的代码被组织成多个包,每个包中包含Java类和接口
- ​	模块的数据则包括资源文件和其他静态信息

模块声明文件

- 模块声明文件:module-info.java
- 新的关键字:module 声明一个模块
- 模块名称的规则 与Java包的命名规则相似



## JDk 10

- JEP286,var局部变量类型推断
- JEP296,将原来用Mercurial管理的众多JDK仓库代码,合并到一个仓库中,简化开发和管理过程
- JEP304,统一的垃圾回收接口
- JEP307,G1垃圾回收器的并行完整垃圾回收,实现并行性来改善最坏情况下的延迟
- JEP310,应用程序类数据(AppCDS)共享,通过跨进共享通用类元数据来减少内存占用空间,和减少启动时间
- JEP312,ThreadLocal握手交互.在不进入到全局JVM安全点(Safepoint)的情况下,对线程执行回调,优化可以只停止单个线程,而不是停全部线程或一个都不停
- JEP313,移除JDK中附带的javah工具,可以使用javac -h代替
- JEP314,使用附加的Unicode语言标记扩展
- JEP317,能将堆内存占用分配给用户指定的备用内存设备
- JEP317,使用Graal基于Java的编译器,可以预先把Java代码编译成本地代码来提升效能
- JEP318,在OpenJDK中提供一组默认的根证书颁发机构证书,开源目前Oracle提供的JavaSE的根证书,这样OpenJDK对开发人员使用起来更方便
- JEP322,基于时间定义的发布版本,即上述提到的发布周期,版本号为...分别是大版本,中间版本,升级包和补丁版本

​	

## JDK 11

- 181:Nest_Based访问控制
- 309:动态类文件常量
- 315:改善Aarch64 intrinsic
- 318:无操作垃圾收集器ZGC
- 320:消除Java EE和CORBA模块
- 321:HTTP客户端(标准)
- 323:局部变量的语法?参数
- 324:Curve25519和Curve448关键协议
- 327:Unicode 10
- 328:飞行记录器
- 329:ChaCha20和Poly1305加密算法
- 330:发射一列纵队源代码程序
- 331:低开销堆分析
- 332:传输层安全性(Transport Layer Security,TLS)1.3
- 333:动作,一个可伸缩的低延迟垃圾收集器(实验)
- 335:反对Nashorn JavaScript引擎
- 336:反对Pack200工具和API



## JDK 12

改进switch，引入->，直接返回结果

```
int num = switch(value) {
    case 1 ->1;
    case 2 ->2;
    case 3,4 ->3;
    case 5: break 5;
    case 6 -> {System.out.println("hello world");//没有返回则抛出异常}
    }
```



## JDK 13

- switch支持如下写法

- ```
   int date = switch (day) {
          case MONDAY, FRIDAY, SUNDAY : yield 6;
          case TUESDAY                : yield 7;
          case THURSDAY, SATURDAY     : yield 8;
          case WEDNESDAY              : yield 9;
          default                     : yield 1; // default条件是必须的
          };
  ```

- 支持文本块 使用三个"""，起始的"""结尾必须换行，文本块是在13中是预览功能,使用前需要先启用

  - 手动编译:

    ​       javac --release 13 --enable-preview ...

    ​     手动运行

    ​       java --enable-preview ...



## JDK 14

- 改进的switch表达式在14中获得了完全的支持

- instanceof支持模式匹配(语言特性 预览特性)

  - 使用instanceof和instanceof里的对象不用强转了

- NullPointerException(JVM特性)

  - 14的这个异常更加详细,精确到某个属性

- Record(预览功能)

  - java14提供了解决get,set,toString等代码冗余,

  - Record会提供equals,hashCode,toString方法的实体,可以重构如下

    - ```
      public record Clazz(int a,double b,String c) {}
      ```

  -  通过record,可以自动地得到equals,hashCode,toString的实现,还有构造器和getter方法



## JDK 15

- 文本块转正
- ZGC垃圾回收器转正
- Shenandoah转正
- 移除 Nashorn JavaScript Engine(JDK脚本执行引擎)
  - 在JDK11标记过期,15完全移除
- CharSequence新增isEmpty默认方法



## JDK 16

- 向量API（孵化）

  - 提供了jdk.incubator.vector来用于矢量计算

- 启用c++ 14 语言特性

- 从Mercurial迁移到Git

- 迁移到GitHub（将OpenJDK源码的版本控制迁移到github上）

- ZGC 并发线程堆栈处理

- Unix-Domain 套接字通道

  - 为socket channel和server-socket channel api增加Unix-domain(AF_UNIX)套接字支持。

- Alpine Linux Port

- Elastic Metaspace

- Windows/AArch64 Port

- Foreign Linker API（孵化）

  - 提供jdk.incubator.foreign来简化native code的调用

- 基于值的类的警告

  - 提供注解 @jdk.internal.ValueBased 来用于标注作为value-based的类
  -  将原始包装类指定为基于值的类，并弃用它们的构造函数以便删除，并提示新的弃用警告。提供关于在Java平台中任何基于值类的实例上进行同步的警告。

- 打包工具

  - 在JDK 16中转正，从 jdk.incubator.jpackage 转为 jdk.jpackage
  - 用于打包自包含的Java应用程序

- 外部内存访问API（第三次孵化）

  - 引入一个API，允许Java程序安全有效地访问Java堆之外的外部内存

- 为 instanceof 进行模式匹配,在JDK 16中转正，可以如下使用

  - ```
    Object o = new String("");
    if (o instanceof String str) {
    	System.out.println(str.concat("模式匹配"));
    }
    ```

  - Records 转正

  - Sealed Classes



## JDK 17

https://www.oracle.com/java/technologies/javase/17-relnote-issues.html

- Sealed Classes(密封类)

  - 已将密封类添加到Java语言中。密封类和接口限制其他类或接口可以扩展或实现它们。
  - 密封类由JEP360提出，并在JDK15中作为预览特性提供。JEP 397再次提出了这些建议，并进行了改进

- Pattern Matching for switch (Preview)

  - 使用开关表达式和语句的模式匹配以及模式语言的扩展来增强Java编程语言。
  - 通过将模式匹配扩展到switch，可以针对多个模式对表达式进行测试，每个模式都有一个特定的操作，因此可以简洁而安全地表达复杂的面向数据的查询。

- New macOS Rendering Pipeline(新的MacOS渲染管道)

  - Swing API用于渲染的Java 2D API现在可以使用新的Apple Metal加速渲染API for macOS。
  - 这在默认情况下目前是禁用的，因此渲染仍然使用OpenGL API，虽然Apple不推荐使用这些API，但它们仍然可用并受支持。
  - 要启用Metal，应用程序应通过设置系统属性指定其用途：-Dsun.java2d.metal=true
  - Metal或OpenGL的使用对应用程序是透明的，因为这是内部实现的差异，对JavaAPI没有影响。
  - 金属管道需要macOS 10.14.x或更高版本。在早期版本上设置它的尝试将被忽略。

- New API for Accessing Large Icons

  - JDK 17中提供了一种新方法

  - javax.swing.filechooser.FileSystemView.getSystemIcon（File，int，int）

  - 它可以在可能的情况下访问更高质量的图标。

  - 它在Windows平台上完全实现；但是，在其他平台上的结果可能会有所不同，稍后将进行增强。例如，通过使用以下代码：

    ```
    FileSystemView fsv = FileSystemView.getFileSystemView();
    Icon icon = fsv.getSystemIcon(new File("application.exe"), 64, 64);
    JLabel label = new JLabel(icon);
    ```

  - 用户可以为“application.exe”文件获取更高质量的图标。

  - 此图标适用于创建可在高DPI环境中更好缩放的标签。

- DatagramSocket Can Be Used Directly to Join Multicast Groups

  - 在此版本中，java.net.DatagramSocket已更新，以添加对加入多播组的支持。
  - 它现在定义了joinGroup和leaveGroup方法来加入和离开多播组。
  - java.net.DatagramSocket的类级API文档已经更新，以解释如何配置普通DatagramSocket并使用它加入和退出多播组。
  - 此更改意味着DatagramSocket API可以用于多播应用程序，而无需使用传统的java.net.MulticastSocket API。

- MulticastSocket API与以前一样工作，尽管它的大多数方法都不推荐使用。

  - 有关这一变化的基本原理的更多信息，请参见CSR JDK-8260667。
  - Add support for UserDefinedFileAttributeView on macOS(在macOS上添加对UserDefinedFileAttributeView的支持)
  - macOS上的文件系统提供程序实现已在此版本中更新，以支持扩展属性。
  - 现在可以使用java.nio.file.attribute.UserDefinedFileAttributeView API获取文件扩展属性的视图。
  - 以前的JDK版本不支持此（可选）视图。

- Enhanced Pseudo-Random Number Generators(增强型伪随机数发生器)

  - Provide new interface types and implementations for pseudorandom number generators (PRNGs), including jumpable PRNGs and an additional class of splittable PRNG algorithms (LXM).
  - 为伪随机数生成器（PRNG）提供新的接口类型和实现，包括可跳线PRNG和附加的可拆分PRNG算法（LXM）。

- Modernization of Ideal Graph Visualizer(理想图形可视化仪的现代化)

  - Source Details in Error Messages(错误消息中的源详细信息)
  - New Page for "New API" and Improved "Deprecated" Page(“新API”的新页面和改进的“弃用”页面)

- Foreign Function & Memory API (Incubator)(外部函数和内存API（孵化器）)

- Console Charset API(控制台字符集API)

  - 已更新java.io.Console以定义一个新方法，该方法返回控制台的字符集。
  - 返回的字符集可能不同于从Charset.defaultCharset（）方法返回的字符集。
  - 例如，它返回IBM437，而Charset.defaultCharset（）在windows上返回windows-1252（en-US）。有关更多详细信息，请参阅CSR。

- Flight Recorder Event for Deserialization(用于反序列化的飞行记录器事件)

- Implement Context-Specific Deserialization Filters(实现特定于上下文的反序列化过滤器)

- 弃用 Applet API 以进行删除

  - 它基本上无关紧要，因为所有 Web 浏览器供应商都已取消对 Java 浏览器插件的支持或宣布了这样做的计划。
  - Java 9 中的JEP 289先前已弃用 Applet API，但并未将其删除。

- 弃用安全管理器以进行删除

  - 安全管理器和与其相关的 API 已被弃用，并将在未来版本中删除。
  - 为确保开发人员和用户知道安全管理器已被弃用，如果通过java -Djava.security.manager.
  - 如果通过System::setSecurityManagerAPI动态启用安全管理器，Java 运行时还会在运行时发出警告。无法禁用这些警告。

- 弃用 Kerberos 中的 3DES 和 RC4

- 弃用套接字实现工厂机制

  ```
  static void ServerSocket.setSocketFactory?(SocketImplFactory fac)
  static void Socket.setSocketImplFactory?(SocketImplFactory fac)
  static void DatagramSocket.setDatagramSocketImplFactory?(DatagramSocketImplFactory fac)
  ```

  - 以下用于设置系统范围套接字实现工厂的静态方法已被弃用

- 弃用 JVM TI 堆函数 1.0



## JDK 18

https://www.oracle.com/java/technologies/javase/18-relnote-issues.html

- [用于核心库改进和更新]
  - 默认编码UTF-8(java.nio.charsets)
  - 从 JDK 18 开始，UTF-8 是 Java SE API 的默认字符集.依赖于默认字符集的 API 现在在所有 JDK 实现中表现一致，并且独立于用户的操作系统、语言环境和配置

- 简单的 Web 服务器(java.net)

  - jwebserver, 一个用于启动最小静态 Web 服务器的命令行工具, 已经被引入.
  - 该工具和随附的 API 位于 jdk.httpserver 模块的 com.sun.net.httpserver 包中，旨在用于原型设计、临时编码和测试，尤其是在教育环境中。

- 使用方法句柄重新实现核心反射(java.lang:reflect)

  - 使用方法句柄重新实现核心反射。依赖于现有实现的高度实现特定和未记录方面的代码可能会受到影响。可能出现的问题包括：

        检查内部生成的反射类（例如 的子类MagicAccessorImpl）的代码不再有效，必须更新。
        尝试破坏封装并将 的私有 finalmodifiers字段Method的值更改为Field与Constructor基础成员不同的类的代码可能会导致运行时错误。必须更新此类代码。

-	为了减轻这种兼容性风险，您可以启用旧实现作为解决方法

​		方法是使用 -Djdk.reflect.useDirectMethodHandle=false.

​		我们将在未来的版本中删除旧的核心反射实现。-Djdk.reflect.useDirectMethodHandle=false解决方法将在此时停止工作。

- 互联网地址解析 SPI(java.net)

  > 为主机名和地址解析引入服务提供者接口 (SPI)，以便 java.net.InetAddress 可以使用平台内置解析器以外的解析器。这个新的 SPI 允许替换操作系统的本地解析器，该解析器通常配置为使用本地主机文件和域名系统 (DNS) 的组合。

[用于工具改进]

- Java API文档中的代码片段(javadoc)

  > 为 JavaDoc 的标准 Doclet 添加了一个 @snippet 标记，以简化在 API 文档中包含示例源代码。

[用于预览和孵化器]

- Vector API（第三个孵化器）(核心库)

  > 引入一个 API 来表达向量计算，该计算可以在运行时可靠地编译为支持的 CPU 架构上的最佳向量指令，从而实现优于等效标量计算的性能。

- 外部函数和内存 API（第二个孵化器）(核心库)

  > 引入一个 API，Java 程序可以通过该 API 与 Java 运行时之外的代码和数据进行互操作。 通过有效地调用外部函数（即 JVM 之外的代码）和安全地访问外部内存（即不受 JVM 管理的内存），API 使 Java 程序能够调用本机库并处理本机数据，而不会出现脆弱性和危险。 JNI。

[规范/语言]

- switch 的模式匹配（第二次预览）

  > 通过对 switch 表达式和语句的模式匹配以及对模式语言的扩展来增强 Java 编程语言。将模式匹配扩展到 switch 允许针对多个模式测试表达式，每个模式都有特定的操作，因此可以简洁安全地表达复杂的面向数据的查询。



## JDK 19

[并发模型更新预览]

- JEP 425虚拟线程（预览）

  > 将虚拟线程引入Java平台。虚拟线程是轻量级线程，可以大大减少编写、维护和观察高吞吐量并发应用程序的工作量。这是一个预览API。

- JEP 428结构化并发（孵化器）

  > 通过引入结构化并发API简化多线程编程。结构化并发将在不同线程中运行的多个任务视为单个工作单元，从而简化错误处理和消除，提高可靠性，增强可观察性。这是一个孵化API。

[语言功能预览]

- JEP 405 Recoord模式（预览）

  > 使用Record模式增强Java编程语言，以解构记录值。可以嵌套Record模式和类型模式，以实现强大、声明性和可组合的数据导航和处理形式。这是一个预览语言功能。

- JEP 427 switch模式匹配（第三次预览）

  > 使用switch表达式和语句的模式匹配增强Java编程语言。将模式匹配扩展到switch允许根据多个模式对表达式进行测试，每个模式都有一个特定的操作，从而可以简洁、安全地表达复杂的面向数据的查询。这是一个预览语言功能。


[Libraries 预览/孵化器]

- JEP 424外部函数和内存API（预览）

  > 引入一个API，通过该API，Java程序可以与Java运行时之外的代码和数据进行互操作。通过有效地调用外部函数（即JVM外部的代码），并通过安全地访问外部内存（即不由JVM管理的内存），API使Java程序能够调用本机库并处理本机数据，而不会出现JNI的脆弱性和危险性。这是一个预览API。

- JEP 426 Vector API（第四培养箱）

  > 引入一个API来表示向量计算，该API在运行时可靠地编译为支持的CPU架构上的最优向量指令，从而实现优于等效标量计算的性能。

[新功能]-增强功能

[core-libs/java.lang]

- 支持Unicode 14.0

  > java.lang.Character类支持14.0级的Unicode字符数据库，该数据库增加了838个字符，总共144697个字符。这些新增内容包括5个新脚本，总共159个脚本，以及37个新表情符号字符。

- 新的System.properties, System.out和System.err

  > 这些系统属性的值是标准输出和标准错误流（system.out和system.err）使用的编码。

[core-libs/java.net]

- 对Java GSS/Kerberos的HTTPS通道绑定支持
  [core-libs/java.time]

增加 Date-Time 格式

- java.time.format中引入了 java.time.format.DateTimeFormatter/DateTimeFormatterBuilder 类

  

  

[core-libs/java.util:collections]

- 创建预分配哈希映射和哈希集的新方法

- 引入了新的静态工厂方法，允许创建预先分配的HashMap和相关实例，以容纳预期数量的映射或元素。

  

[security-libs/java.security]

- Windows KeyStore更新为包括对本地计算机位置的访问
- 在X509Certificate:：getSubjectAlternativeNames和X509Certificate：：getIssuerAlternationalNames中分解序列

[security-libs/javax.net.ssl]

- TLS签名方案

[security-libs/jdk.security]

- 向jarsigner添加-providerPath选项

[hotspot/compiler]

- 支持Linux/AArch64上的PAC-RET保护

[hotspot/runtime]

- CDS档案的自动生成
  ……


[删除的功能和选项]

- 删除诊断标志GCParallelVerificationEnabled

- 删除SSLSocketImpl中的终结器实现
- 删除Subject:：current和Subject::callAs API的替代线程本地实现

​    
[不推荐的功能和选项]

- java.lang.ThreadGroup已降级
- Locale 类构造函数的弃用
- PSSParameterSpec（int）构造函数和DEFAULT静态常量已弃用
- OAEPParameterSpec类DEFAULT静态常量已弃用

————————————————
版权声明：本文为CSDN博主「HackShendi」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/qq_41806966/article/details/108647463