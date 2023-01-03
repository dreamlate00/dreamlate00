# Docker

> 什么是容器

容器（Container）：容器是一种轻量级、可移植、并将应用程序进行的打包的技术，使应用程序可以在几乎任何地方以相同的方式运行，Docker将镜像文件运行起来后，产生的对象就是容器。容器相当于是镜像运行起来的一个实例且容器具备一定的生命周期。

---

> Docker容器和虚拟机的区别

相同点：

- 容器和虚拟机一样，都会对物理硬件资源进行共享使用。
- 容器和虚拟机的生命周期比较相似（创建、运行、暂停、关闭等等）。
- 容器中或虚拟机中都可以安装各种应用，如redis、mysql、nginx等。也就是说，在容器中的操作，如同在一个虚拟机(操作系统)中操作一样。
- 同虚拟机一样，容器创建后，会存储在宿主机上：linux上位于/var/lib/docker/containers下

不同点：

- 虚拟机的创建、启动和关闭都是基于一个完整的操作系统。一个虚拟机就是一个完整的操作系统。而容器直接运行在宿主机的内核上，其本质上以一系列进程的结合。
- 容器是轻量级的，虚拟机是重量级的。首先容器不需要额外的资源来管理(不需要Hypervisor、Guest OS)，虚拟机额外更多的性能消耗；其次创建、启动或关闭容器，如同创建、启动或者关闭进程那么轻松，而创建、启动、关闭一个操作系统就没那么方便了。
  也因此，意味着在给定的硬件上能运行更多数量的容器，甚至可以直接把Docker运行在虚拟机上。

---

> Docker能做什么

Docker 可以让你将所有应用软件以及它的以来打包成软件开发的标准化单元。

Docker 容器将软件以及它运行安装所需的一切文件（代码、运行时、系统工具、系统库）打包到一起，这就保证了不管是在什么样的运行环境，总是能以相同的方式运行。

---

> Docker版本介绍

Docker有Docker-CE和Docker-EE两种。

Docker-CE指Docker社区版，由社区维护和提供技术支持，为免费版本，适合个人开发人员和小团队使用。

Docker-EE指Docker企业版，为收费版本，由售后团队和技术团队提供技术支持，专为企业开发和IT团队而设计。
相比Docker-EE，增加一些额外功能，更重要的是提供了更安全的保障。

此外，Docker的发布版本分为Stable版和Edge版，区别在于前者是按季度发布的稳定版(发布慢)，后者是按月发布的边缘版(发布快)。

通常情况下，Docker-CE足以满足我们的需求。

---

## Linux下安装Docker

> Centos

```
# step 1: 安装必要的一些系统工具
sudo yum install -y yum-utils device-mapper-persistent-data lvm2
# Step 2: 添加软件源信息
sudo yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
# Step 3: 更新并安装Docker-CE
sudo yum makecache fast
sudo yum -y install docker-ce
# Step 4: 开启Docker服务
sudo service docker start
```

---

> Ubuntu:

```python
# step 1: 安装必要的一些系统工具
sudo apt-get update
sudo apt-get -y install apt-transport-https ca-certificates curl software-properties-common
# step 2: 安装GPG证书
curl -fsSL http://mirrors.aliyun.com/docker-ce/linux/ubuntu/gpg | sudo apt-key add -
# Step 3: 写入软件源信息
sudo add-apt-repository "deb [arch=amd64] http://mirrors.aliyun.com/docker-ce/linux/ubuntu $(lsb_release -cs) stable"
# Step 4: 更新并安装Docker-CE
sudo apt-get -y update
sudo apt-get -y install docker-ce
```

---

> 验证

```
docker version
```

---

## 阿里云Docker镜像加速

1. 进入aliyun.com
2. 搜索容器镜像服务
3. 设置Registry登录密码开通服务
4. 登陆
5. 在左侧点击镜像中心 -> 镜像加速器
6. 获取每个人的加速器地址
7. 根据相应操作系统的操作文档操作

## Docker中央仓库

```
hub.docker.com
```

镜像和容器默认存储在

```
cd /var/lib/docker
```

## Docker镜像管理

![](Docker.assets/image-management.svg)

信息

```
docker image ls
docker images

docker image inspect
docker inspect

docker history
```

### 镜像搜索

命令格式

```
docker search [参数] 搜索项
```

常用参数

```
-f # 根据提供的格式筛选结果
--limit int # 展示最大的结果数，默认为25
```

### 镜像下载

命令格式

如果不指定版本号，默认下载latest

```
docker pull [参数] 镜像名称[:版本号]
```

### 镜像查看

命令格式

```
docker images [参数] [仓库名[版本号]]
docker image ls [参数] [仓库名[版本号]]
```

常用参数

```
-a # 展示所有的镜像
-q # 只展示镜像ID
```

显示格式

`仓库名 版本号 镜像ID 创建时间`

### 镜像删除

命令格式

```
docker rm [参数] 镜像名[镜像名..] -- 可以同时删除一个或多个本地镜像
```

常用参数

```
-f # 强制删除
```

### 镜像保存备份

命令格式

```
docker save [参数] 镜像名[镜像名..] | 镜像ID[镜像ID..] -- 可以打包一个或多个镜像保存成本地tar文件
```

常用参数

```
-o # 指定写入的文件名和路径，默认为STDOUT
```

导出（保存）时建议指定镜像的名称最好不要使用镜像ID，否则备份导入时镜像名称与版本号会显示none

### 镜像备份导入

命令格式

```
docker load [参数]
```

常用参数

```
-i # 指定要导入的文件默认为STDIN
```

### 镜像重命名

命令格式

```
docker rename [源镜像] [新镜像]
```

### 镜像历史信息

可以使用这个命令查看镜像在之前的更改操作

命令格式

```
docker history [参数] 镜像
```

### 镜像详细信息

命令格式

```
docker image inspect [参数] 镜像 [镜像...]
docker inspect [参数] 镜像 [镜像...]
```

常用参数

```
-f # 利用特定Go语言的format格式输出结果
```

不带参数的使用docker inspect 会打印长串的信息(标准的json格式)，所以推荐使用 -f 参数查看指定的信息

## 镜像与容器

![](Docker.assets/container-image.svg)

### 容器提交

根据容器生成一个新的镜像

命令格式

```
docker commit [参数] 容器[版本]
```

常用参数

```
-a # 添加作者
-c # 为创建的镜像加入Dockerfile命令
-m # 类似git commit -m
-p # 提交时暂停容器
```

### 容器导出

将当前容器导出为TAR文件

命令格式

```
docker export [参数] 容器
```

常用参数

```
-o # 指定写入的文件
```

### 容器导入

将之前导出的容器文件导入并创建为一个镜像

命令格式

```
docker import [参数] 文件|链接|[版本信息]
```

常用参数

```
-m # 导入时添加提交信息
-c # 为创建的镜像加入Dockerfile命令
```

> docker import 与 docker commit 的区别

当你使用docker import 时，导入的镜像是一个全新镜像，是无法使用docker history查看到镜像的历史信息，使用docker commit 导入时，生成的镜像可以使用docker history查看到镜像的历史信息的。

---

### 深入理解Docker容器和镜像

http://blog.itpub.net/31556785/viewspace-2565389/

以后再说

## Docker常用命令

```
# 启动
systemctl start docker
# 守护进程重启
sudo systemctl daemon-reload
# 重启docker服务
systemctl restart  docker
# 重启docker服务
sudo service docker restart
# 关闭docker 
service docker stop 
# 关闭docker 
systemctl stop docker
# 从远程仓库抽取镜像
docker pull 镜像名<:tags>
# 查看本地镜像
docker images
# 创建容器启动应用
docker run 镜像名<:tags>
# 查看正在运行中的镜像
docker ps <-a>
# 删除容器
docker rm <-f> 容器id
# 删除镜像
docker rmi <-f> 镜像名:<tags>
```

docker宿主机和容器端口映射

```
docker run -p 8000:8080 tomcat
```

后台运行

```
docker run -p -d 8000:8080 tomcat
```

停止

```
docker ps
docker stop 容器id
```

## 移除镜像

```
docker rmi tomcat:8.5.53-jdk8-openjdk
```

## Tomcat容器内部结构

![image-20200410121619473](Docker.assets/tomcat-container.svg)

在容器中执行命令，exec：在对应容器中执行命令 -it：采用交互的方式执行命令

```
dockerexec<-it> 容器id 命令
```

进入docker容器内部 

```
docker exec -it 容器(tomcat)id /bin/bash
```

退出

```
exit
```

## 容器生命周期

![image-20200410122927429](Docker.assets/docker-scope.svg)

### 容器创建

命令格式

```
docker create [参数] 镜像名称[容器执行命令][执行命令时需要提供的参数]
```

常用参数

```
-t # 分配一个虚拟终端
-i # 提供一个模拟输入，不提供则无法输入默认命令
--name # 为创建好的容器提供一个容器名，不提供的话会随机起一个
```

### 容器启动

启动一个或多个容器

命令格式

```
docker start [参数] 容器 [容器...]
```

常用参数

```
-a # 将当前的输入/输出链接到容器上
-i # 将当前的输入链接到容器上
```

### 容器创建并启动

命令格式

```
docker run [参数] 镜像 [容器执行命令] [执行命令提供的参数]
```

常用参数

```
-t # 分配一个虚拟终端
-i # 保持输入打开
-d # 容器后台运行，并打印容器id
--rm # 容器结束后自动删除容器
```

docker run 以及 docker create/start 的一些参数的具体的区别在这里简单记两个公式

```
docker run = doker create + docker start -a
docker run -d = docker create + docker start
```

推荐大家使用`docker run -dti`来启动所需容器

### 容器暂停

命令格式

```
docker pause/unpause 容器 [容器..]
```

### 容器关闭

命令格式

```
docker stop 容器 [容器..]
```

常用参数

```
-t # 关闭前的等待时间，默认是10秒
```

### 容器终止

命令格式

```
docker kill [参数] 容器 [容器..]
```

常用参数

```
-s # 指定发给容器的关闭信号，默认为“kill”
```

### 容器重启

命令格式

```
docker restart [参数] 容器[容器..]
```

常用参数

```
-f # 强制删除
-v # 删除容器的同时删除容器的数据卷
```

> docker kill 和 docker stop 区别

linux下关于终止进程的信号:SIGTERM 和 SIGKILL

SIGKILL信号：无条件终止进程信号。进程接收到该信号会立即终止，不进行清理和暂存工作。该信号不能被忽略、处理和阻塞，它向系统管理员提供了可以杀死任何进程的方法。

SIGTERM信号：程序终结信号，可以由kill命令产生。与SIGKILL不同的是，SIGTERM信号可以被阻塞和终止，以便程序在退出前可以保存工作或清理临时文件等。

docker stop 会先发出SIGTERM信号给进程，告诉进程即将会被关闭。在-t指定的等待时间过了之后，将会立即发出SIGKILL信号，直接关闭容器。

docker kill 直接发出SIGKILL信号关闭容器。但也可以通过-s参数修改发出的信号。

docker restart 中同样可以设置 -t 等待时间，当等待时间过后会立刻发送SIGKILL信号，直接关闭容器。

因此会发现在docker stop的等待过程中，如果终止docker stop的执行，容器最终没有被关闭。而docker kill几乎是立刻发生，无法撤销。

---



```
#创建容器但不运行
docker create 容器名
#运行容器
docker start 容器id
#暂停运行
docker pause 容器id
#从暂停中恢复
docker unpause 容器id
#停止容器运行
docker stop 容器id
```



## Dockerfile构建镜像

Dockerfile镜像描述文件，是一个包含用于组合镜像的命令的文本文档 -> 本身是一个脚本

Docker通过读取Dockerfile中的指令按步骤自动生成镜像

docker build -t 机构/镜像名<:tags> Dockerfile目录

步骤

![image-20200428213320801](Docker.assets/image-20200428213320801.png)

Dockerfile

```
# 基准镜像
FROME tomcat:latest
MAINTAINER zhaoyansong.ink
# 切换工作目录，不存在则创建 WORKDIR与cd差不多
WORKDIR /user/local/tomcat/webapps
# 复制目录下的所有文件到容器目录
#   文件夹名     webapps目录下的docker-web目录
ADD docker-web ./docker-web
```

使用xftp将first-dockerfile文件移动到linux下的usr/images中

进入目录first-dockerfile

-t 添加名字 目录（就是当前目录所以是 .）

```
docker build -t zhaoyansong/mywebapp:1.0 .
```

运行

```
docker run -d -p 8001:8080  zhaoyansong/mywebapp:1.0
```

### Dockerfile自动部署Tomcat应用

```
#设置基准镜像
FROM tomcat:latest
#说明当前镜像是Xxx谁维护的
MAINTAINER mashibing.com
#用于切换工作目录
WORKDIR /usr/local/tomcat/webapps
#将指定的文件或目录复制到镜像的指定目录下
ADD docker-web ./docker-web 
```

用xftp将文件传送到虚拟机

```
#-t:名字和版本  之后写安装地址
docker build -t mashibing.com/mywebapp:1.0 /usr/image/first-dockerfile/
```

## 镜像分层（layer）

![image-20200428213125906](Docker.assets/image-layer.svg)

容器层

镜像层



![image-20200428213906793](Docker.assets/image-20200428213906793.png)

vim

基于centos

![image-20200428213858575](Docker.assets/image-20200428213858575.png)

:wq

```
docker build -t zhaoyansong/docker_layer:1.0 .
```

做出修改

![image-20200428214241074](Docker.assets/image-20200428214241074.png)

重新构建

```
docker build -t zhaoyansong/docker_layer:1.1 .
```

前三步为Using cache 

后面做出的改变从新处理

## Dockerfile基础指令

### FROM

基于基准镜像

```
FORM centos # 制作基准镜像(基于centos:lastest)
FORM scratch # 不依赖任何基准镜像base image
FORM tomcat:9.0.22-jdk8-openjdk
尽量使用官方的base Image
```

### LABEL&MAINTAINER

说明信息

```
MAINTAINER zhaoyansong.ink
LABEL version = "1.0"
LABEL description = "赵岩松手记"
```

### WORKDIR

设置工作目录

```
WORKDIR /usr/local
WORKDIR /usr/local/newdir # 自动创建
尽量使用绝对路径
```

### ADD&COPY

复制文件

```
ADD hello / # 复制到根路径
ADD test.tar.gz / # 添加根路径目录并解压缩
ADD 除了复制，还具备添加远程文件的功能
```

### ENV

设置环境变量

```
ENV JAVA_HOME /usr/local/openjdk8
RUN ${JAVA_HOME}/bin/java -jar test.jar
尽量使用环境常量，可提高程序维护性
```

## Dockerfile执行指令

### RUN&CMD&ENTRYPOINT

```
RUN：在Build构建时执行命令
ENTRYPOINT：容器启动时执行的命令
CMD：容器启动后执行默认的命令或参数
```

### 不同的执行时机

![image-20200428215520596](Docker.assets/执行时机.svg)

### RUN

构建时运行

```,
RUN yum install -y vim # Shell命令格式
RUN ["yum","install","-y","vim"] # Exec命令格式
```

### Shell运行方式

使用Shell执行时，当前Shell是父进程，生成一个子Shell进程

在子Shell中执行脚本。脚本执行完毕，退出子Shell，回到当前Shell



![image-20200428215646361](Docker.assets/shell-way.svg)

### Exec运行方式

使用Exec方式，会用Exec进程替换当前进程，并且保持PID不变

执行完毕，直接退出，并不会退回之前的进程环境



![image-20200428215723164](Docker.assets/exec-way.svg)

### ENTRYPOINT

启动命令

```
ENTRYPOINT(入口点)用于在启动容器时执行命令
Dockerfile中只有最后一个ENTRYPOINT会被执行
ENTRYPOINT ["ps"] # 推荐使用Exec格式
```

### CMD

默认命令

CMD用于设置默认执行的命令

如Dockerfile中出现多个CMD，则只有最后一个被执行

如容器启动时附加指令，则CMD被忽略

CMD ["ps","-ef"] # 推荐使用Exec格式

## 容器间Link单向通信

虚拟IP

![image-20200428221802598](Docker.assets/accross.svg)

如果换了一个mysql镜像，那么虚拟容器会变化，如果使用虚拟IP的话还需要改配置

**给容器起名字，使用名字连接**

![image-20200428222850524](Docker.assets/image-20200428222850524.png)

## Bridge网桥双向通信

![image-20200428223001787](Docker.assets/bridge-way.svg)

查看网桥

```
docker network ls
```

创建新的网桥

```
docker network create -d bridge my-bridge
```

将网桥与容器关联

```
docker network connect my-bridge web
docker network connect my-bridge database
```

### 网桥实现原理



![image-20200428223952739](Docker.assets/bridge1.svg)

每安装一个网桥就在宿主机上安装一个虚拟网卡

## Volum容器间共享数据



![image-20200428224411710](Docker.assets/accross-data01.svg)

### 通过设置-v挂载宿主机目录

格式

```
docker run --name 容器名 -v 宿主机路径:容器内挂载路径 镜像名
```

通过--volumes-from共享容器内挂载点

创建容器

`--name webpage -v /webapps:/tomcat/webapps` 

```
docker create --name webpage -v /webapps:/tomcat/webapps tomcat /bin/true
```

共享容器挂载点

 `--volumes-from webpage` 

```
docker run --volumes-from webpage --name t1 -d tomcat
```

## Docker Compose

官方提供的官方容器编排工具

单机多容器部署工具

通过yaml文件定义多容器如何部署

WIN/MAC默认提供，Linux需要自行安装

**多容器部署的麻烦事**

![image-20200503203424310](Docker.assets/bother.svg)

