## Java7

- switch中添加对String类型的支持

- 数字字面量的改进 / 数值可加下划

- 异常处理（捕获多个异常） try-with-resources

- 增强泛型推断

- JSR203 NIO2.0（AIO）新IO的支持

- JSR292与InvokeDynamic指令

- Path接口、DirectoryStream、Files、WatchService（重要接口更新）

- fork/join framework

  - Java7提供的一个用于并行执行任务的框架，是一个把大任务分割成若干个小任务，最终汇总每个小任务结果后得到大任务结果的框架。

    该框架为Java8的并行流打下了坚实的基础



## JAVA8

- Lambda表达式

- Optional

- Stream API

- Date/Time API (JSR 310)

- JavaScript引擎Nashorn

- Base64

- 接口的默认方法与静态方法

  - ```java
    public interface Action{
    	default String hello(){return "hello world";}
    	static String hi(){return "hello world";}
    }
    ```

-  方法引用（含构造方法引用）.通常与Lambda表达式联合使用，可以直接引用已有Java类或对象的方法

  - 构造器引用。语法是Class::new，要求构造器方法是没有参数
  - 静态方法引用。语法是Class::static_method，要求接受一个Class类型的参数
  - 特定类的任意对象方法引用。它的语法是Class::method。要求方法是没有参数的
  - 特定对象的方法引用，它的语法是instance::method。要求方法接受一个参数，与3不同的地方在于，3是在列表元素上分别调用方法，而4是在某个对象上调用方法，将列表元素作为参数传入

- 重复注解

  - 在Java 5中使用注解有一个限制，即相同的注解在同一位置只能声明一次。Java 8引入重复注解，这样相同的注解在同一地方也可以声明多次。重复注解机制本身需要用@Repeatable注解。Java 8在编译器层做了优化，相同注解会以集合的方式保存，因此底层的原理并没有变化。

- 扩展注解的支持（类型注解）

  - Java 8扩展了注解的上下文，几乎可以为任何东西添加注解，包括局部变量、泛型类、父类与接口的实现，连方法的异常也能添加注解。

- 其他

  - 更好的类型推测机制：Java 8在类型推测方面有了很大的提高，这就使代码更整洁，不需要太多的强制类型转换了。
  - 编译器优化：Java 8将方法的参数名加入了字节码中，这样在运行时通过反射就能获取到参数名，只需要在编译时使用-parameters参数。
  - 并行（parallel）数组：支持对数组进行并行处理，主要是parallelSort()方法，它可以在多核机器上极大提高数组排序的速度。
  - 并发（Concurrency）：在新增Stream机制与Lambda的基础之上，加入了一些新方法来支持聚集操作。
  - Nashorn引擎jjs：基于Nashorn引擎的命令行工具。它接受一些JavaScript源代码为参数，并且执行这些源代码。
  - 类依赖分析器jdeps：可以显示Java类的包级别或类级别的依赖。
  - JVM的PermGen空间被移除：取代它的是Metaspace（JEP 122）。

