创建docker私服

```
docker run -d -p 5000:5000 --restart=always --name registry-local registry:latest
```

由于docker默认镜像仓库是dockerhub，所以java:my相当于docker.io/java:my，因此，想要将镜像推送到私服仓库中，需要修改镜像标签。



### 修改 daemon.json 文件

 vim /etc/docker/daemon.json

```
{
  "registry-mirrors": ["https://yy28v837.mirror.aliyuncs.com"],
  "insecure-registries":["192.168.243.128:5000"]
}
```

　registry-mirrors 是配置的国内下载镜像的地址，因为默认拉取镜像是从国外地址拉取，导致下载镜像速度非常慢。

  insecure-registries:["宿主机ip:容器端口号"]（不该这个会报错）

### 1.5 重启 docker 服务

systemctl restart docker

### 1.6启动 registry 镜像

docker start localregistry 



```
docker tag java:my localhost:5000/java:my
docker push localhost:5000/java:my
```

拉取私服镜像

```
docker pull localhost:5000/java:my
c
curl http://localhost:5000/v2/_catalog
```

