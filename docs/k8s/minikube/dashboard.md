创建dashboard

```
minikube dashboard
```

查看所有pod，现在只关心dashboard

```
minikube kubectl -- get pods -A
```

pod的详细信息

```
kubectl describe kubernetes-dashboard-84d7457d44-9wmf8 --namespace kubernetes-dashboard
```

使用proxy代理到宿主机的指定端口

```
kubectl proxy --port=8001 --address='192.168.234.201' --accept-hosts='^.*' &
```

访问地址

```
http://192.168.234.201:8001/api/v1/namespaces/kubernetes-dashboard/services/http:kubernetes-dashboard:/proxy/#/workloads?namespace=default
```



另一个思路

创建一个service，用NodePort方式暴露出接口供外部调用

使用nginx代理这个外部接口