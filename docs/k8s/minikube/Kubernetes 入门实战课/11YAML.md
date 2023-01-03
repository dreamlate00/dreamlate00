参考API

```
kubectl explain pod
kubectl explain pod.metadata
kubectl explain pod.spec
kubectl explain pod.spec.containers
```

空运行并且生成YAML文件

```
kubectl run ngx --image=nginx:alpine --dry-run=client -o yaml
```

