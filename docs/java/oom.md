# OOM

**OutOfMemoryError**

在Java程序中,JVM无法为应用程序分配所需的内存而导致的错误。

- 堆内存
- 方法区
- 本地方法栈
- 虚拟机栈



原因：JVM 没有足够的内存来为对象分配空间,并且垃圾回收器也没有可回收的空间。由此又引出了内存泄漏和内存溢出。



内存泄漏: 申请使用完的内存没有释放，导致虚拟机不能再次使用该内存，此时这段内存就泄露了。因为申请者不用了，而又不能被虚拟机分配给别人用.

简单点就是已经用完的对象没有办法被回收，导致虚拟机没有可以分片的内存。

内存溢出：申请的内存超出了 JVM 能提供的内存大小



内存泄漏持续存在，最后一定会溢出



常见的OOM

java.lang.OutOfMemoryError: PermGen space

java.lang.OutOfMemoryError: Metadata space

java.lang.StackOverflowError