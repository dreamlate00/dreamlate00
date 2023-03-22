```
FROM
　　基于哪个镜像
MAINTAINER
　　用来写备注信息，例如作者、日期等。
COPY
　　复制文件进入镜像（只能用相对路径，不能用绝对路径）
ADD
　　复制文件进入镜像（可以用绝对路径，假如是压缩文件会解压）
WORKDIR
　　指定工作目录，假如路径不存在会创建路径
ENV
　　设置环境变量
EXPOSE
　　暴露容器端口到宿主机
RUN
　　在构建镜像的时候执行一条命令，作用于镜像层面
　　shell命令格式：RUN yum install -y net-tools
　　exec命令格式：RUN [ "yum","install" ,"-y" ,"net-tools"]
ENTRYPOINT
　　在容器启动的时候执行，作用于容器层，dockerfile里有多条时只允许执行最后一条
CMD
　　在容器启动的时候执行，作用于容器层，dockerfile里有多条时只允许执行最后一条
　　容器启动后执行默认的命令或者参数，允许被修改
```

demo

```
# 基于cntos7，如果没有这个镜像那么它会下载这个镜像。
FROM centos:7
# 创建者(这个可以不写)
MAINTAINER 吴磊
# 为Dockerfile中所有RUN、CMD、ENTRYPOINT、COPY和ADD指令设定工作目录
WORKDIR /usr
# 执行命令(这里创建了一个目录)
RUN mkdir /usr/local/java
# 和copy一样，复制文件到指定目录，但是copy不能解压，add自动解压
ADD jdk-8u181-linux-x64.tar.gz /usr/local/java
# 重命名(不知道文件名可以现在宿主机解压后看一下)
RUN ln -s /usr/local/java/jdk1.8.0_181 /usr/local/java/jdk 

# 设置环境变量 
ENV JAVA_HOME /usr/local/java/jdk 
ENV JRE_HOME ${JAVA_HOME}/jre 
ENV CLASSPATH .:${JAVA_HOME}/lib:${JRE_HOME}/lib 
ENV PATH ${JAVA_HOME}/bin:$PATH 

执行dockerfile文件，创建的镜像名为jdk1.8，最后面的点表示当前目录(即dockerfile所在目录) 
[root@localhost file]# docker build -t='jdk1.8' . 
```

