busybox是一个很好的调试工具，可以测试k8s集群的很多问题。其中包含了很多常用的命令，比如：ping、[wget](https://so.csdn.net/so/search?q=wget&spm=1001.2101.3001.7020)、telnet等日常运维需要用到的一些常用工具。虽然不支持curl,但是是可以使用wget的。

```
apiVersion: v1
kind: Pod
metadata:
  name: busybox
  namespace: default
spec:
  containers:
  - name: busybox
    image: busybox:1.28.4  #最新版的nslookup有点问题，因此这里指定版本
    command:  #这里使用命令夯住busybox,不然它执行完一次就completed了
      - sleep
      - "3600"
    imagePullPolicy: IfNotPresent
  restartPolicy: Always
```

创建pod

```
kubectl apply -f busybox.yaml
```

进入容器

```
kubectl exec -it busybox -- sh
```



备用镜像

```
registry.cn-chengdu.aliyuncs.com/qzcsbj/busybox:1.28
```

