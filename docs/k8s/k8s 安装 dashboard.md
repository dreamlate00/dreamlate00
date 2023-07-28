```
wget https://raw.githubusercontent.com/kubernetes/dashboard/v2.0.0/aio/deploy/recommended.yaml

kubectl apply -f recommended.yaml
```



```
kubectl get pods -A
kubectl get svc -A
```



删除kubernetes-dashboard的service

```
kubectl delete service kubernetes-dashboard --namespace=kubernetes-dashboard
```



创建yaml文件，改为NodePort端口暴露方式去访问

```
kind: Service
apiVersion: v1
metadata:
  labels:
    k8s-app: kubernetes-dashboard
  name: kubernetes-dashboard
  namespace: kubernetes-dashboard
spec:
  type: NodePort
  ports:
    - port: 443
      targetPort: 8443
  selector:
    k8s-app: kubernetes-dashboard
```

执行

```
kubectl apply -f dashboard-svc.yaml
```



想要访问dashboard服务，就要有访问权限，创建kubernetes-dashboard管理员角色

```
 vim dashboard-svc-account.yaml
 
 apiVersion: v1
kind: ServiceAccount
metadata:
  name: dashboard-admin
  namespace: kube-system
---
kind: ClusterRoleBinding
apiVersion: rbac.authorization.k8s.io/v1
metadata:
  name: dashboard-admin
subjects:
  - kind: ServiceAccount
    name: dashboard-admin
    namespace: kube-system
roleRef:
  kind: ClusterRole
  name: cluster-admin
  apiGroup: rbac.authorization.k8s.io
```

重新执行

```
 kubectl create -f dashboard-svc-account.yaml
```



获取token

```
kubectl get secret -n kube-system | grep admin
kubectl describe secret dashboard-admin-token-p5s5z -n kube-system | grep '^token'
```

复制粘贴刚刚生成的token，就OK了