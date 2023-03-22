> docker: Got permission denied while trying to connect to the Docker daemon socket at unix:///var/run/docker.sock: Post http://%2Fvar%2Frun%2Fdocker.sock/v1.24/containers/create: dial unix /var/run/docker.sock: connect: permission denied.

原因

Docker 进程使用 Unix Socket，而 `/var/run/docker.sock` 需要 root 权限才能进行读写操作。

如果不考虑安全问题的话，也可以使用 root 权限直接改写 `/var/run/docker.sock` 文件的权限，使得其对所有普通用户都有读写权限： sudo chmod 666 /var/run/docker.sock 

解决办法

```
sudo groupadd docker			# 有则不用创建
sudo usermod -aG docker USER	# USER 为加入 docker 组的用户
newgrp docker					# 刷新 docker 组
docker run hello-world			# 测试无 root 权限能否使用 docker
```

