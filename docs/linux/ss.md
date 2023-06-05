##### 1.列出已建立的连接

默认情况下，如果我们运行ss命令而没有指定其他选项，它将显示所有已建立连接的打开的非侦听套接字的列表，例如TCP，UDP或UNIX套接字。

```
[root@renwolecom ~]# ss | head -n 5
Netid  State  Recv-Q Send-Q Local Address:Port   Peer Address:Port
u_str  ESTAB  0      0       * 19098                 * 18222
u_str  ESTAB  0      0       * 19441                 * 19440
u_str  ESTAB  0      0       * 19440                 * 19441
u_str  ESTAB  0      0       * 19396                 * 19397
```



##### 2.显示监听tcp套接字

我们可以使用-l选项专门列出当前正在侦听连接的套接字，而不是列出所有的套接字。

```
[root@renwolecom ~]# ss -lt
State   Recv-Q Send-Q    Local Address:Port       Peer Address:Port
LISTEN  0      128                   *:http                  *:*
LISTEN  0      100           127.0.0.1:smtp                  *:*
LISTEN  0      128                   *:entexthigh            *:*
LISTEN  0      128       172.28.204.62:zabbix-trapper        *:*
LISTEN  0      128           127.0.0.1:cslistener            *:*
LISTEN  0      80                   :::mysql                :::*
LISTEN  0      100                 ::1:smtp                 :::*
LISTEN  0      128                  :::entexthigh           :::*
```

在这个示例中，我们还使用-t选项只列出TCP，稍后将对此进行详细说明。在后面的例子中，你会看到我将结合多种选择，以快速过滤掉，从而达到我们的目的。

### 3.显示进程

我们可以用-p选项打印出拥有套接字的进程或PID号。

```
[root@renwolecom ~]# ss -pl

Netid  State      Recv-Q Send-Q Local Address:Port     Peer Address:Port
tcp    LISTEN     0      128    :::http                :::*                 users:(("httpd",pid=10522,fd=4),("httpd",pid=10521,fd=4),("httpd",pid=10520,fd=4),("httpd",pid=10519,fd=4),("httpd",pid=10518,fd=4),("httpd",pid=10516,fd=4))
在上面的例子中我只列出了一个结果，没有进行进一步选项，因为ss的完整输出打印出超过500行到标准输出。所以我只列出一条结果，由此我们可以看到服务器上运行的各种Apache进程ID。
```

4.不解析服务名称

默认情况下，ss只会解析端口号，例如在下面的行中，我们可以看到172.28.204.62:mysql，其中mysql被列为本地端口。

```
[root@renwolecom ~]# ss
Netid State Recv-Q Send-Q   Local Address:Port      	Peer Address:Port
tcp   ESTAB 0      0 ::ffff:172.28.204.62:mysql ::ffff:172.28.204.62:38920
tcp   ESTAB 0      0 ::ffff:172.28.204.62:mysql ::ffff:172.28.204.62:51598
tcp   ESTAB 0      0 ::ffff:172.28.204.62:mysql ::ffff:172.28.204.62:51434
tcp   ESTAB 0      0 ::ffff:172.28.204.62:mysql ::ffff:172.28.204.62:36360

```

但是，如果我们指定-n选项，看到的是端口号而不是服务名称。

```
[root@renwolecom ~]# ss -n
Netid State Recv-Q Send-Q   Local Address:Port      	Peer Address:Port
tcp   ESTAB 0      0 ::ffff:172.28.204.62:3306  ::ffff:172.28.204.62:38920
tcp   ESTAB 0      0 ::ffff:172.28.204.62:3306  ::ffff:172.28.204.62:51598
tcp   ESTAB 0      0 ::ffff:172.28.204.62:3306  ::ffff:172.28.204.62:51434
tcp   ESTAB 0      0 ::ffff:172.28.204.62:3306  ::ffff:172.28.204.62:36360
```

现在显示3306，而非mysql，因为禁用了主机名和端口的所有名称解析。另外你还可以查看/etc/services得到所有服务对应的端口列表。

##### 5.解析数字地址/端口

用-r选项可以解析IP地址和端口号。用此方法可以列出172.28.204.62服务器的主机名。

```
[root@renwolecom ~]# ss -rNetid  State  Recv-Q Send-Q        Local Address:Port      Peer Address:Port
tcp    ESTAB      0      0         renwolecom:mysql        renwolecom:48134
```

##### 6.IPv4套接字

我们可以通过-4选项只显示与IPv4套接字对应的信息。在下面的例子中，我们还使用-l选项列出了在IPv4地址上监听的所有内容。

```
[root@renwolecom ~]# ss -l4
Netid State      Recv-Q Send-Q  Local Address:Port            Peer Address:Port
tcp   LISTEN     0      128                 *:http                       *:*
tcp   LISTEN     0      100         127.0.0.1:smtp                       *:*
tcp   LISTEN     0      128                 *:entexthigh                 *:*
tcp   LISTEN     0      128     172.28.204.62:zabbix-trapper             *:*
tcp   LISTEN     0      128         127.0.0.1:cslistener                 *:*

```

##### 7.IPv6套接字

同样，我们可以使用-6选项只显示与IPv6套接字相关信息。在下面的例子中，我们还使用-l选项列出了在IPv6地址上监听的所有内容。

```
[root@renwolecom ~]# ss -l6
Netid State      Recv-Q Send-Q  Local Address:Port            Peer Address:Port
udp   UNCONN     0      0                  :::ipv6-icmp                 :::*
udp   UNCONN     0      0                  :::ipv6-icmp                 :::*
udp   UNCONN     0      0                  :::21581                     :::*
tcp   LISTEN     0      80                 :::mysql                     :::*
tcp   LISTEN     0      100               ::1:smtp                      :::*
tcp   LISTEN     0      128                :::entexthigh                :::*

```

##### 8.只显示TCP

-t选项只显示TCP套接字。当与-l结合只打印出监听套接字时，我们可以看到所有在TCP上侦听的内容。

```
[root@renwolecom ~]# ss -lt
State       Recv-Q Send-Q    Local Address:Port               Peer Address:Port
LISTEN      0      128                   *:http                          *:*
LISTEN      0      100           127.0.0.1:smtp                          *:*
LISTEN      0      128                   *:entexthigh                    *:*
LISTEN      0      128       172.28.204.62:zabbix-trapper                *:*
LISTEN      0      128           127.0.0.1:cslistener                    *:*
LISTEN      0      80                   :::mysql                        :::*
LISTEN      0      100                 ::1:smtp                         :::*
LISTEN      0      128                  :::entexthigh                   :::*

```

##### 9.显示UDP

-u选项可用于仅显示UDP套接字。由于UDP是一种无连接的协议，因此只运行-u选项将不显示输出，我们可以将它与-a或-l选项结合使用，来查看所有侦听UDP套接字，如下所示：

```
[root@renwolecom ~]# ss -ul
State      Recv-Q Send-Q Local Address:Port                 Peer Address:Port
UNCONN     0      0              *:sunwebadmins                 *:*
UNCONN     0      0              *:etlservicemgr                *:*
UNCONN     0      0              *:dynamid                      *:*
UNCONN     0      0              *:9003                         *:*
UNCONN     0      0              *:9004                         *:*
UNCONN     0      0      127.0.0.1:terabase                     *:*
UNCONN     0      0              *:56803             
```

​    
10. ##### Unix套接字

-x选项只能用来显示unix域套接字。

```
[root@renwolecom ~]# ss -x
Netid State Recv-Q Send-Q                    Local Address:Port Peer Address:Port
u_str ESTAB 0      0 /tmp/zabbix_server_preprocessing.sock 23555           * 21093
u_str ESTAB 0      0          /tmp/zabbix_server_ipmi.sock 20155           * 19009
u_str ESTAB 0      0 /tmp/zabbix_server_preprocessing.sock 19354           * 22573
u_str ESTAB 0      0 /tmp/zabbix_server_preprocessing.sock 21844           * 19375
```



##### 11.显示所有信息

-a选项显示所有的监听和非监听套接字，在TCP的情况下，这意味着已建立的连接。这个选项与其他的组合很有用，例如可以添加-a选项显示所有的UDP套接字，默认情况下只有-u选项我们看不到多少信息。

```
[root@renwolecom ~]# ss -u
Recv-Q Send-Q Local Address:Port                 Peer Address:Port
0      0      172.28.204.66:36371                    8.8.8.8:domain
```

```
[root@renwolecom ~]# ss -ua
State      Recv-Q Send-Q Local Address:Port                 Peer Address:Port
UNCONN     0      0                 *:sunwebadmins                    *:*
UNCONN     0      0                 *:etlservicemgr                   *:*
UNCONN     0      0                 *:dynamid                         *:*
UNCONN     0      0                 *:9003                            *:*
UNCONN     0      0                 *:9004                            *:*
UNCONN     0      0         127.0.0.1:terabase                        *:*
UNCONN     0      0                 *:56803                           *:*
ESTAB      0      0      172.28.204.66:36371                     8.8.8.8:domain
```



##### 12.显示套接字内存使用情况

-m选项可用于显示每个套接字使用的内存量。

```
[root@renwolecom ~]# ss -ltm
State   Recv-Q Send-Q  Local Address:Port Peer Address:Port
LISTEN  0      128                 *:http           *:*skmem:(r0,rb87380,t0,tb16384,f0,w0,o0,bl0)
LISTEN  0      100         127.0.0.1:smtp           *:*skmem:(r0,rb87380,t0,tb16384,f0,w0,o0,bl0)
LISTEN  0      128                 *:entexthigh     *:*skmem:(r0,rb87380,t0,tb16384,f0,w0,o0,bl0)
LISTEN  0      128     172.28.204.62:zabbix-trapper *:*skmem:(r0,rb87380,t0,tb16384,f0,w0,o0,bl0)
LISTEN  0      128         127.0.0.1:cslistener     *:*skmem:(r0,rb87380,t0,tb16384,f0,w0,o0,bl0)
LISTEN  0      80                 :::mysql         :::*skmem:(r0,rb87380,t0,tb16384,f0,w0,o0,bl0)
LISTEN  0      100               ::1:smtp          :::*skmem:(r0,rb87380,t0,tb16384,f0,w0,o0,bl0)
LISTEN  0      128                :::entexthigh    :::*skmem:(r0,rb87380,t0,tb16384,f0,w0,o0,bl0)
```



##### 13.显示TCP内部信息

我们可以使用-i选项请求额外的内部TCP信息。

```
[root@renwolecom ~]# ss -lti
State       Recv-Q Send-Q Local Address:Port         Peer Address:Port
LISTEN      0      128               *:chimera-hwm              *:* 	bbr cwnd:10
LISTEN      0      128               *:etlservicemgr            *:* 	bbr cwnd:10
LISTEN      0      128   172.28.204.66:27017                    *:* 	bbr cwnd:10
LISTEN      0      128       127.0.0.1:27017                    *:* 	bbr cwnd:10
LISTEN      0      128               *:dynamid                  *:* 	bbr cwnd:10
LISTEN      0      128               *:9003                     *:* 	bbr cwnd:10
LISTEN      0      128               *:9004                     *:* 	bbr cwnd:10
LISTEN      0      128               *:http                     *:* 	bbr cwnd:10
LISTEN      0      128               *:ssh                      *:* 	bbr cwnd:10
LISTEN      0      100       127.0.0.1:smtp                     *:* 	bbr cwnd:10
LISTEN      0      128               *:sunwebadmins             *:* 	bbr cwnd:10
LISTEN      0      128              :::ssh                     :::* 	bbr cwnd:10
```


在每个侦听套接字下面，我们可以看到更多信息。注意：-i选项不适用于UDP，如果您指定-u，而非-t，则不会显示这些额外的信息。

##### 14.显示统计信息

我们可以使用-s选项快速查看统计数据。

* ```
  [root@renwolecom ~]# ss -s
  Total: 798 (kernel 1122)
  TCP:   192 (estab 99, closed 81, orphaned 0, synrecv 0, timewait 1/0), ports 0
  
  Transport Total     IP        IPv6
  
  *         1122      -         -
            RAW       1         0         1
            UDP       0         0         0
            TCP       111       59        52
            INET      112       59        53
            FRAG      0         0         0
  这使我们能够快速看到已建立连接的总数，及各种类型的套接字的计数和IPv4或IPv6的使用情况。
  ```

##### 15.基于状态的过滤器

我们可以指定一个套接字的状态，只打印这个状态下的套接字。例如，我们可以指定包括已建立， established, syn-sent, syn-recv, fin-wait-1, fin-wait-2, time-wait, closed, closed-wait, last-ack监听和关闭等状态。以下示例显示了所有建立的TCP连接。为了生成这个，我通过SSH连接到了服务器，并从Apache加载了一个网页。然后我们可以看到与Apache的连接迅速转变为等待时间。

```
[root@renwolecom ~]# ss -t state established
Recv-Q Send-Q       Local Address:Port              Peer Address:Port
0      52           172.28.204.67:ssh              123.125.71.38:49518
0      0     ::ffff:172.28.204.67:http      ::ffff:123.125.71.38:49237

[root@renwolecom ~]# ss -t state established
Recv-Q Send-Q       Local Address:Port              Peer Address:Port
0      0            172.28.204.67:ssh            103.240.143.126:55682
0      52           172.28.204.67:ssh              123.125.71.38:49518
0      0     ::ffff:172.28.204.67:http      ::ffff:123.125.71.38:49262
```



##### 16.根据端口号进行过滤

可以通过过滤还可以列出小于(lt)，大于(gt)，等于(eq)，不等于(ne)，小于或等于(le)，或大于或等于(ge)的所有端口。

例如，以下命令显示端口号为500或以下的所有侦听端口：

```
[root@renwolecom ~]# ss -ltn sport le 500
State       Recv-Q Send-Q Local Address:Port        Peer Address:Port
LISTEN      0      128                *:80                     *:*
LISTEN      0      100        127.0.0.1:25                     *:*
LISTEN      0      100              ::1:25                    :::*
```


为了进行比较，我们可以执行相反的操作，并查看大于500的所有端口：

```
[root@renwolecom ~]# ss -ltn sport gt 500
State       Recv-Q Send-Q Local Address:Port        Peer Address:Port
LISTEN      0      128                *:12002                  *:*
LISTEN      0      128    172.28.204.62:10051                  *:*
LISTEN      0      128        127.0.0.1:9000                   *:*
LISTEN      0      80                :::3306                  :::*
LISTEN      0      128               :::12002                 :::*
```


我们还可以根据源或目标端口等项进行筛选，例如，我们搜索具有SSH源端口运行的TCP套接字：

```
[root@renwolecom ~]# ss -t '( sport = :ssh )'
State       Recv-Q Send-Q    Local Address:Port     Peer Address:Port
ESTAB       0      0         172.28.204.66:ssh     123.125.71.38:50140
```



##### 17.显示SELinux上下文

-Z与-z选项可用于显示套接字的SELinux安全上下文。 在下面的例子中，我们使用-t和-l选项来列出侦听的TCP套接字，使用-Z选项我们也可以看到SELinux的上下文。

```
[root@renwolecom ~]# ss -tlZ
State  Recv-Q Send-Q  Local Address:Port        Peer Address:Port
LISTEN 0      128                 *:sunrpc                 *:*
users:(("systemd",pid=1,proc_ctx=system_u:system_r:init_t:s0,fd=71))
LISTEN 0      5       172.28.204.62:domain                 *:*
users:(("dnsmasq",pid=1810,proc_ctx=system_u:system_r:dnsmasq_t:s0-s0:c0.c1023,fd=6))
LISTEN 0      128                 *:ssh                    *:*
users:(("sshd",pid=1173,proc_ctx=system_u:system_r:sshd_t:s0-s0:c0.c1023,fd=3))
LISTEN 0      128         127.0.0.1:ipp                    *:*
users:(("cupsd",pid=1145,proc_ctx=system_u:system_r:cupsd_t:s0-s0:c0.c1023,fd=12))
LISTEN 0      100         127.0.0.1:smtp                   *:*
users:(("master",pid=1752,proc_ctx=system_u:system_r:postfix_master_t:s0,fd=13))
```



##### 18.显示版本号

-v选项可用于显示ss命令的特定版本信息，在这种情况下，我们可以看到提供ss的iproute包的版本。

```
[root@renwolecom ~]# ss -v
ss utility, iproute2-ss130716
```



##### 19.显示帮助文档信息

-h选项可用于显示有关ss命令的进一步的帮助，如果需要对最常用的一些选项进行简短说明，则可以将其用作快速参考。 请注意：这里并未输入完整列表。

```
[root@renwolecom ~]# ss -h
Usage: ss [ OPTIONS ]
```



##### 20.显示扩展信息

我们可以使用-e选项来显示扩展的详细信息，如下所示，我们可以看到附加到每条行尾的扩展信息。

```
[root@renwolecom ~]# ss -lte
State  Recv-Q Send-Q Local Address:Port   Peer Address:Port
LISTEN 0      128                *:sunrpc *:*      ino:16090 sk:ffff880000100000 <->
LISTEN 0      5      172.28.204.62:domain *:*      ino:23750 sk:ffff880073e70f80 <->
LISTEN 0      128                *:ssh    *:*      ino:22789 sk:ffff880073e70000 <->
LISTEN 0      128        127.0.0.1:ipp    *:*      ino:23091 sk:ffff880073e707c0 <->
LISTEN 0      100        127.0.0.1:smtp   *:*      ino:24659 sk:ffff880000100f80 <->
```

21.显示计时器信息

##### 

-o选项可用于显示计时器信息。该信息向我们展示了诸如重新传输计时器值、已经发生的重新传输的数量以及已发送的keepalive探测的数量。

登录后复制

```
[root@renwolecom ~]# ss -to
State      Recv-Q Send-Q Local Address:Port      Peer Address:Port
ESTAB      0      52     172.28.204.67:ssh      123.125.71.38:49518timer:(on,406ms,0)
LAST-ACK   0      1      172.28.204.67:ssh    103.240.143.126:49603timer:(on,246ms,0)
```



Linux下的21个ss命令使用示例详解
https://blog.51cto.com/net881004/2166100



```
NAME
       ss - another utility to investigate sockets

SYNOPSIS
       ss [options] [ FILTER ]

DESCRIPTION
       ss  is  used to dump socket statistics. It allows showing information similar to netstat.  It can display more TCP and state informations
       than other tools.


OPTIONS
       When no option is used ss displays a list of open non-listening sockets (e.g. TCP/UNIX/UDP) that have established connection.

       -h, --help
              Show summary of options.

       -V, --version
              Output version information.

       -H, --no-header
              Suppress header line.

       -n, --numeric
              Do not try to resolve service names.

       -r, --resolve
              Try to resolve numeric address/ports.

       -a, --all
              Display both listening and non-listening (for TCP this means established connections) sockets.

       -l, --listening
              Display only listening sockets (these are omitted by default).

       -o, --options
              Show timer information.

       -e, --extended
              Show detailed socket information

       -m, --memory
              Show socket memory usage.

       -p, --processes
              Show process using socket.

       -i, --info
              Show internal TCP information.

       -K, --kill
              Attempts to forcibly close sockets. This option displays sockets that are successfully closed and silently skips sockets that  the
              kernel does not support closing. It supports IPv4 and IPv6 sockets only.

       -s, --summary
              Print summary statistics. This option does not parse socket lists obtaining summary from various sources. It is useful when amount
              of sockets is so huge that parsing /proc/net/tcp is painful.

       -Z, --context
              As the -p option but also shows process security context.

              For netlink(7) sockets the initiating process context is displayed as follows:

                     1.  If valid pid show the process context.

                     2.  If destination is kernel (pid = 0) show kernel initial context.

                     3.  If a unique identifier has been allocated by the kernel or netlink user, show context as "unavailable". This will  gen‐
                         erally indicate that a process has more than one netlink socket active.

       -z, --contexts
              As  the  -Z  option but also shows the socket context. The socket context is taken from the associated inode and is not the actual
              socket context held by the kernel. Sockets are typically labeled with the context of the creating  process,  however  the  context
              shown will reflect any policy role, type and/or range transition rules applied, and is therefore a useful reference.

       -N NSNAME, --net=NSNAME
              Switch to the specified network namespace name.

       -b, --bpf
              Show socket BPF filters (only administrators are allowed to get these information).

       -4, --ipv4
              Display only IP version 4 sockets (alias for -f inet).

       -6, --ipv6
              Display only IP version 6 sockets (alias for -f inet6).

       -0, --packet
              Display PACKET sockets (alias for -f link).

       -t, --tcp
              Display TCP sockets.

       -u, --udp
              Display UDP sockets.

       -d, --dccp
              Display DCCP sockets.

       -w, --raw
              Display RAW sockets.

       -x, --unix
              Display Unix domain sockets (alias for -f unix).

       -S, --sctp
              Display SCTP sockets.

       --vsock
              Display vsock sockets (alias for -f vsock).

       -f FAMILY, --family=FAMILY
              Display sockets of type FAMILY.  Currently the following families are supported: unix, inet, inet6, link, netlink, vsock.

       -A QUERY, --query=QUERY, --socket=QUERY
              List  of  socket  tables  to  dump, separated by commas. The following identifiers are understood: all, inet, tcp, udp, raw, unix,
              packet, netlink, unix_dgram, unix_stream, unix_seqpacket, packet_raw, packet_dgram, dccp, sctp, vsock_stream, vsock_dgram.

       -D FILE, --diag=FILE
              Do not display anything, just dump raw information about TCP sockets to FILE after applying filters. If FILE is - stdout is used.

       -F FILE, --filter=FILE
              Read filter information from FILE.  Each line of FILE is interpreted like single command line option. If FILE is - stdin is used.

       FILTER := [ state STATE-FILTER ] [ EXPRESSION ]
              Please take a look at the official documentation (Debian package iproute-doc) for details regarding filters.


STATE-FILTER
       STATE-FILTER allows to construct arbitrary set of states to match. Its syntax is sequence of keywords state and exclude followed by iden‐
       tifier of state.

       Available identifiers are:

              All  standard TCP states: established, syn-sent, syn-recv, fin-wait-1, fin-wait-2, time-wait, closed, close-wait, last-ack, listen
              and closing.

              all - for all the states

              connected - all the states except for listen and closed

              synchronized - all the connected states except for syn-sent

              bucket - states, which are maintained as minisockets, i.e.  time-wait and syn-recv

              big - opposite to bucket


USAGE EXAMPLES
       ss -t -a
              Display all TCP sockets.

       ss -t -a -Z
              Display all TCP sockets with process SELinux security contexts.

       ss -u -a
              Display all UDP sockets.

       ss -o state established '( dport = :ssh or sport = :ssh )'
              Display all established ssh connections.

       ss -x src /tmp/.X11-unix/*
              Find all local processes connected to X server.

       ss -o state fin-wait-1 '( sport = :http or sport = :https )' dst 193.233.7/24
              List all the tcp sockets in state FIN-WAIT-1 for our apache to network 193.233.7/24 and look at their timers.

SEE ALSO
       ip(8), /usr/share/doc/iproute-doc/ss.html (package iproutedoc),
       RFC 793 - https://tools.ietf.org/rfc/rfc793.txt (TCP states)


AUTHOR
       ss was written by Alexey Kuznetsov, <kuznet@ms2.inr.ac.ru>.

       This manual page was written by Michael Prokop <mika@grml.org> for the Debian project (but may be used 
```

ss -h

```
Usage: ss [ OPTIONS ]
       ss [ OPTIONS ] [ FILTER ]
   -h, --help          this message
   -V, --version       output version information
   -n, --numeric       don't resolve service names
   -r, --resolve       resolve host names
   -a, --all           display all sockets
   -l, --listening     display listening sockets
   -o, --options       show timer information
   -e, --extended      show detailed socket information
   -m, --memory        show socket memory usage
   -p, --processes     show process using socket
   -i, --info          show internal TCP information
   -s, --summary       show socket usage summary
   -b, --bpf           show bpf filter socket information
   -E, --events        continually display sockets as they are destroyed
   -Z, --context       display process SELinux security contexts
   -z, --contexts      display process and socket SELinux security contexts
   -N, --net           switch to the specified network namespace name

   -4, --ipv4          display only IP version 4 sockets
   -6, --ipv6          display only IP version 6 sockets
   -0, --packet        display PACKET sockets
   -t, --tcp           display only TCP sockets
   -S, --sctp          display only SCTP sockets
   -u, --udp           display only UDP sockets
   -d, --dccp          display only DCCP sockets
   -w, --raw           display only RAW sockets
   -x, --unix          display only Unix domain sockets
       --vsock         display only vsock sockets
   -f, --family=FAMILY display sockets of type FAMILY
       FAMILY := {inet|inet6|link|unix|netlink|vsock|help}

   -K, --kill          forcibly close sockets, display what was closed
   -H, --no-header     Suppress header line

   -A, --query=QUERY, --socket=QUERY
       QUERY := {all|inet|tcp|udp|raw|unix|unix_dgram|unix_stream|unix_seqpacket|packet|netlink|vsock_stream|vsock_dgram}[,QUERY]

   -D, --diag=FILE     Dump raw information about TCP sockets to FILE
   -F, --filter=FILE   read filter information from FILE
       FILTER := [ state STATE-FILTER ] [ EXPRESSION ]
       STATE-FILTER := {all|connected|synchronized|bucket|big|TCP-STATES}
         TCP-STATES := {established|syn-sent|syn-recv|fin-wait-{1,2}|time-wait|closed|close-wait|last-ack|listen|closing}
          connected := {established|syn-sent|syn-recv|fin-wait-{1,2}|time-wait|close-wait|last-ack|closing}
       synchronized := {established|syn-recv|fin-wait-{1,2}|time-wait|close-wait|last-ack|closing}
             bucket := {syn-recv|time-wait}
                big := {established|syn-sent|fin-wait-{1,2}|closed|close-wait|last-ack|listen|closing}

```

