![image-20221021100258036](12-POD.assets/image-20221021100258036-16663177797925.png)

![img](12-POD.assets/9ebab7d513a211a926dd69f7535ac175.png)





![img](12-POD.assets/b5a7003788cb6f2b1c5c4f6873a8b5cf.jpg)

创建

```
spec:
  containers:
  - image: busybox:latest
    name: busy
    imagePullPolicy: IfNotPresent
    env:
      - name: os
        value: "ubuntu"
      - name: debug
        value: "on"
    command:
      - /bin/echo
    args:
      - "$(os), $(debug)"
```

```
kubectl apply -f busy-pod.yml
```

删除

```
kubectl delete -f busy-pod.yml
kubectl delete pod busy-pod
```

日志

```
kubectl logs busy-pod
```

查看

```
kubectl get pod
kubectl describe pod busy-pod
```

复制文件

```
echo 'aaa' > a.txt
kubectl cp a.txt ngx-pod:/tmp
```

进入容器

```
kubectl exec -it ngx-pod -- sh
```

