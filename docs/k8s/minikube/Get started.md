安装

Linux

```shell
curl -LO https://storage.googleapis.com/minikube/releases/latest/minikube-linux-amd64
sudo install minikube-linux-amd64 /usr/local/bin/minikube
```

macOS

```
curl -LO https://storage.googleapis.com/minikube/releases/latest/minikube-darwin-amd64
sudo install minikube-darwin-amd64 /usr/local/bin/minikube
```



启动一个集群

```
minikube start
```

交互

所有的pod

```
kubectl get po -A
minikube kubectl -- get po -A
```

别名

```shell
alias kubectl="minikube kubectl --"
```

控制面板

```shell
minikube dashboard
```



部署

```
# 创建pod
kubectl create deployment hello-minikube --image=docker.io/nginx:1.23
# 创建service
kubectl expose deployment hello-minikube --type=NodePort --port=80
# 查看服务的状态
kubectl get services hello-minikube
# 直接端口映射
kubectl port-forward service/hello-minikube 7080:80
```



### LoadBalancer deployments

```
kubectl create deployment balanced --image=docker.io/nginx:1.23
kubectl expose deployment balanced --type=LoadBalancer --port=80

minikube tunnel

kubectl get services balanced
```



管理集群

```
minikube pause
minikube unpause
minikube stop
minikube config set memory 9001
minikube addons list
# 创建另一个集群
minikube start -p aged --kubernetes-version=v1.16.1
# minikube delete --all

```

