使用docker创建redis集群

- 使用docker-compose进行编排
- 创建统一的目录，统一的配置管理



##### 集群配置



脚本创建文件和文件夹

```shell
for port in `seq 8001 8006`; do \
  mkdir -p /data/redis_data/${port}/conf \
  && PORT=${port} envsubst < /root/docker/redis/redis_cluster.conf.template > /root/docker/redis/${port}/conf/redis.conf \
  && mkdir -p /data/redis_data/${port}/data;\
done
```

redis.conf.template

```
```

