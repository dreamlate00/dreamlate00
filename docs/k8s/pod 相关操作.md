查看所有pods

```
kubectl get pod -A
```

进入容器

```
kubectl exec -ti 容器名 -n 命名空间 sh
```





```
kubectl get pods -n namespace
kubectl get pod -A
kubectl get pod -o wide
```

查看容器详情

```
kubectl describe pod busybox
```

查看容器的命令行日志

```
kubectl logs -n kubernetes-dashboard dashboard-metrics-scraper-6f9ff8d98c-smnz2
```

