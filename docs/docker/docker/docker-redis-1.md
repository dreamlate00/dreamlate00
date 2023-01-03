## 用docker-compose的方式创建redis集群

[TOC]

### 配置文件和映射的文件夹创建

```shell
cat > redis-cluster.tmpl <<EOF
# 节点端口
port ${PORT}
# 添加访问认证
requirepass 1234
# 如果主节点开启了访问认证，从节点访问主节点需要认证
masterauth 1234
# 保护模式，默认值 yes，即开启。开启保护模式以后，需配置 bind ip 或者设置访问密码；关闭保护模式，外部网络可以直接访问
protected-mode no
# 是否以守护线程的方式启动（后台启动），默认 no
daemonize no
# 是否开启 AOF 持久化模式，默认 no
appendonly yes
# 是否开启集群模式，默认 no
cluster-enabled yes
# 集群节点信息文件
cluster-config-file nodes.conf
# 集群节点连接超时时间
cluster-node-timeout 15000
# 集群节点 IP，这里需要特别注意一下，如果要对外提供访问功能，需要填写宿主机的 IP，如果填写 Docker 分配的 IP（172.x.x.x），可能会导致外部无法正常访问集群
cluster-announce-ip 192.168.49.142
# 集群节点映射端口
cluster-announce-port ${PORT}
# 集群节点总线端口
cluster-announce-bus-port 1${PORT}
EOF
```



```shell
for port in `seq 6371 6376`; \
do \
mkdir -p ${port}/conf   && \
PORT=${port} envsubst < redis-cluster.tmpl > ${port}/conf/redis.conf   && \
mkdir -p ${port}/data;\
done
```



```shell
cat > docker-compose.yaml <<EOF
version: '3'

# 定义服务，可以多个
services:
  redis-6371: # 服务名称
    image: redis # 创建容器时所需的镜像
    container_name: redis-6371 # 容器名称
    restart: always # 容器总是重新启动
    network_mode: "host" # host 网络模式
    volumes: # 数据卷，目录挂载
      - ./6371/conf/redis.conf:/usr/local/etc/redis/redis.conf
      - ./6371/data:/data
      - ./6371/logs:/usr/local/redis/logs
    command: redis-server /usr/local/etc/redis/redis.conf # 覆盖容器启动后默认执行的命令

  redis-6372:
    image: redis
    container_name: redis-6372
    restart: always
    network_mode: "host"
    volumes:
      - ./6372/conf/redis.conf:/usr/local/etc/redis/redis.conf
      - ./6372/data:/data
      - ./6372/logs:/usr/local/redis/logs
    command: redis-server /usr/local/etc/redis/redis.conf

  redis-6373:
    image: redis
    container_name: redis-6373
    restart: always
    network_mode: "host"
    volumes:
      - ./6373/conf/redis.conf:/usr/local/etc/redis/redis.conf
      - ./6373/data:/data
      - ./6373/logs:/usr/local/redis/logs
    command: redis-server /usr/local/etc/redis/redis.conf

  redis-6374:
    image: redis
    container_name: redis-6374
    restart: always
    network_mode: "host"
    volumes:
      - ./6374/conf/redis.conf:/usr/local/etc/redis/redis.conf
      - ./6374/data:/data
      - ./6374/logs:/usr/local/redis/logs
    command: redis-server /usr/local/etc/redis/redis.conf

  redis-6375:
    image: redis
    container_name: redis-6375
    restart: always
    network_mode: "host"
    volumes:
      - ./6375/conf/redis.conf:/usr/local/etc/redis/redis.conf
      - ./6375/data:/data
      - ./6375/logs:/usr/local/redis/logs
    command: redis-server /usr/local/etc/redis/redis.conf

  redis-6376:
    image: redis
    container_name: redis-6376
    restart: always
    network_mode: "host"
    volumes:
      - ./6376/conf/redis.conf:/usr/local/etc/redis/redis.conf
      - ./6376/data:/data
      - ./6376/logs:/usr/local/redis/logs
    command: redis-server /usr/local/etc/redis/redis.conf
EOF
```



```
docker-compose up -d
```



```bash
docker exec -it redis-6371 bash
redis-cli -a 1234 --cluster create 192.168.50.111:6371 192.168.50.111:6372 192.168.50.111:6373 192.168.50.111:6374 192.168.50.111:6375 192.168.50.111:6376 --cluster-replicas 1
#检查集群状态
redis-cli -a 1234 --cluster check 192.168.50.111:6371
```



### docker-compose的配置文件

### 启动

### 验证

