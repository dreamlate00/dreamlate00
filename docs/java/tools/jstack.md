# Jstack

jstack可以定位到线程堆栈，根据堆栈信息我们可以定位到具体代码，所以它在JVM性能调优中使用得非常多。

```
Usage:
    jstack [-l] <pid>
        (to connect to running process)
    jstack -F [-m] [-l] <pid>
        (to connect to a hung process)
    jstack [-m] [-l] <executable> <core>
        (to connect to a core file)
    jstack [-m] [-l] [server_id@]<remote server IP or hostname>
        (to connect to a remote debug server)
```



状态

```
● runnable，线程处于执行中
● deadlock，死锁（重点关注）
● blocked，线程被阻塞 （重点关注）
● Parked，停止
● locked，对象加锁
● waiting，线程正在等待
● waiting to lock 等待上锁
● Object.wait()，对象等待中
● waiting for monitor entry 等待获取监视器（重点关注）
● Waiting on condition，等待资源（重点关注），最常见的情况是线程在等待网络的读写

java.lang.Thread.State: WAITING (parking)
java.lang.Thread.State: RUNNABLE
```



统计线程数量

```
jstack -l pid | grep 'java.lang.Thread.State' | wc -l
```



查询线程

```
ps -Lfp pid
ps -mp pid -o THREAD，tid，time
top -Hp pid
```



定位线程