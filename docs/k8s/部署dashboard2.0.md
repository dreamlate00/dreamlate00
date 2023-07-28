这个是新的部署



```

# 下载dashboard的部署文件
wget https://raw.githubusercontent.com/kubernetes/dashboard/v2.0.0-beta7/aio/deploy/recommended.yaml
# 增加nodePort和type
sed -i '/targetPort:/a\ \ \ \ \ \ nodePort: 30001\n\ \ type: NodePort' recommended.yaml
实例：

kind: Service
apiVersion: v1
metadata:
  labels:
    k8s-app: kubernetes-dashboard
  name: kubernetes-dashboard
  namespace: kubernetes-dashboard
spec:
  ports:
    - port: 443
      targetPort: 8443
      nodePort: 30001
  type: NodePort
  selector:
    k8s-app: kubernetes-dashboard
    
    
# 执行创建
kubectl create -f kubernetes-dashboard.yaml
```



检查运行状态

```
kubectl get deployment kubernetes-dashboard -n kuberentes-dashboard
kubectl get pods -n kuberentes-dashboard -o wide
kubectl get services -n kuberentes-dashboard
netstat -ntlp|grep 30001
```



在火狐访问 **https://172.16.10.110:30001**



令牌：

创建用户

创建`dashboard-adminuser.yaml`

```
apiVersion: v1
kind: ServiceAccount
metadata:
  name: admin-user
  namespace: kubernetes-dashboard
```

执行 `kubectl apply -f dashboard-adminuser.yaml`



创建`admin-user-role-binding.yaml`

```
apiVersion: rbac.authorization.k8s.io/v1
 kind: ClusterRoleBinding
 metadata:
   name: admin-user
 roleRef:
   apiGroup: rbac.authorization.k8s.io
   kind: ClusterRole
   name: cluster-admin
 subjects:
 - kind: ServiceAccount
   name: admin-user
   namespace: kubernetes-dashboard
```

执行`kubectl create -f admin-user-role-binding.yaml`



生成token

```
kubectl -n kubernetes-dashboard create token admin-user
```



## Getting a long-lived Bearer Token for ServiceAccount

We can also create a token with the secret which bound the service account and the token will be saved in the Secret:

```
apiVersion: v1
kind: Secret
metadata:
  name: admin-user
  namespace: kubernetes-dashboard
  annotations:
    kubernetes.io/service-account.name: "admin-user"   
type: kubernetes.io/service-account-token  
```



After Secret is created, we can execute the following command to get the token which saved in the Secret:

```
kubectl get secret admin-user -n kubernetes-dashboard -o jsonpath={".data.token"} | base64 -d
```