# 如何让应用不宕机

单节点应用有宕机的风险

如何快速扩容或者收缩访问

问题

POD可以把restartPolicy属性设置为Always，可以监控 Pod 里容器的状态，一旦发生异常，就会自动重启容器。

“restartPolicy”只能保证容器正常工作。如果出错或者被删除，pod将会消失



设置副本数量



```
apiVersion: apps/v1
kind: Deployment
metadata:
  name: xxx-dep
```



```
export out="--dry-run=client -o yaml"kubectl 
create deploy ngx-dep --image=nginx:alpine $out
```



```
apiVersion: apps/v1
kind: Deployment
metadata:
  labels:
    app: ngx-dep
  name: ngx-dep
  
spec:
  replicas: 2
  selector:
    matchLabels:
      app: ngx-dep
      
  template:
    metadata:
      labels:
        app: ngx-dep
    spec:
      containers:
      - image: nginx:alpine
        name: nginx
```



```
kubectl apply -f deploy.yml
```



```
kubectl get deploy
```



```
kubectl get pod
```

动态伸缩

kubectl scale 是命令式操作，扩容和缩容只是临时的措施，如果应用需要长时间保持一个确定的 Pod 数量，最好还是编辑 Deployment 的 YAML 文件，改动“replicas”，再以声明式的 kubectl apply 修改对象的状态。

```
kubectl scale --replicas=5 deploy ngx-dep
```



Pod 只能管理容器，不能管理自身，所以就出现了 Deployment，由它来管理 Pod。

Deployment 里有三个关键字段，其中的 template 和 Job 一样，定义了要运行的 Pod 模板。

replicas 字段定义了 Pod 的“期望数量”，Kubernetes 会自动维护 Pod 数量到正常水平。

selector 字段定义了基于 labels 筛选 Pod 的规则，它必须与 template 里 Pod 的 labels 一致。

创建 Deployment 使用命令 kubectl apply，应用的扩容、缩容使用命令 kubectl scale。