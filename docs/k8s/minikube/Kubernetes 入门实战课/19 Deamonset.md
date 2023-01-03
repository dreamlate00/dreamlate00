# 如何部署一个守护进程

如何实现在每个节点都运行一个守护进程一样的app，执行日志收集、节点监控这样的任务



**Daemonset**

DaemonSet控制器能够确保k8s集群所有的节点都运行一个相同的pod副本，当向k8s集群中增加node节点时，这个node节点也会自动创建一个pod副本，当node节点从集群移除，这些pod也会自动删除；删除Daemonset也会删除它们创建的pod

和Deployment 的不同：**每个 Node 上最多只能运行一个副本**。

```
# 编写一个DaemonSet资源清单
[root@k8s-master1 ~]# cat daemonset.yaml 
apiVersion: apps/v1
kind: DaemonSet
metadata:
  name: fluentd-elasticsearch
  namespace: kube-system
  labels:
    k8s-app: fluentd-logging
spec:
  selector:
    matchLabels:
      name: fluentd-elasticsearch
  template:
    metadata:
      labels:
        name: fluentd-elasticsearch
    spec:
      tolerations:
      - key: node-role.kubernetes.io/master
        effect: NoSchedule
      containers:
      - name: fluentd-elasticsearch
        image: xianchao/fluentd:v2.5.1
        resources:
          limits:
            memory: 200Mi
          requests:
            cpu: 100m
            memory: 200Mi
        volumeMounts:
        - name: varlog
          mountPath: /var/log
        - name: varlibdockercontainers
          mountPath: /var/lib/docker/containers
          readOnly: true
      terminationGracePeriodSeconds: 30
      volumes:
      - name: varlog
        hostPath:
          path: /var/log
      - name: varlibdockercontainers
        hostPath:
          path: /var/lib/docker/containers

[root@k8s-master1 ~]# kubectl apply -f daemonset.yaml 
daemonset.apps/fluentd-elasticsearch created

[root@k8s-master1 ~]# kubectl get ds -n kube-system
NAME                    DESIRED   CURRENT   READY   UP-TO-DATE   AVAILABLE   NODE SELECTOR            AGE
calico-node             3         3         3       3            3           kubernetes.io/os=linux   2d12h
fluentd-elasticsearch   3         3         3       3            3           <none>                   8s
kube-proxy              3         3         3       3            3           kubernetes.io/os=linux   2d13h

[root@k8s-master1 ~]# kubectl get pods -n kube-system -o wide -l name=fluentd-elasticsearch
NAME                          READY   STATUS    RESTARTS   AGE   IP               NODE          NOMINATED NODE   READINESS GATES
fluentd-elasticsearch-9hjqc   1/1     Running   0          83s   10.244.159.134   k8s-master1   <none>           <none>
fluentd-elasticsearch-bdfs6   1/1     Running   0          83s   10.244.36.124    k8s-node1     <none>           <none>
fluentd-elasticsearch-wh44b   1/1     Running   0          83s   10.244.169.152   k8s-node2     <none>           <none>

# 镜像更新
# kubectl set image daemonsets fluentd-elasticsearch fluentd-elasticsearch=image_name -n kube-system
```



按照 DaemonSet 的本意，应该在每个节点上都运行一个 Pod 实例才对，但 Master 节点却被排除在外了

默认情况下，master节点是不参与调度的，且在master节点上有一个污点NoSchedule（表示k8s将不会将Pod调度到具有该污点的Node上）

污点（taint）和容忍度（toleration）

“污点”是 Kubernetes 节点的一个属性，它的作用也是给节点“贴标签”，但为了不和已有的 labels 字段混淆，就改成了 taint。

和“污点”相对的，就是 Pod 的“容忍度”，顾名思义，就是 Pod 能否“容忍”污点。

Kubernetes 在创建集群的时候会自动给节点 Node 加上一些“污点”，方便 Pod 的调度和部署。可以用 kubectl describe node 来查看 Master 和 Worker 的状态：

```

kubectl describe node master

Name:     master
Roles:    control-plane,master
...
Taints:   node-role.kubernetes.io/master:NoSchedule
...

kubectl describe node worker

Name:     worker
Roles:    <none>
...
Taints:   <none>
...
```

Master 节点默认有一个 taint，名字是 node-role.kubernetes.io/master，它的效果是 NoSchedule，也就是说这个污点会拒绝 Pod 调度到本节点上运行，而 Worker 节点的 taint 字段则是空的。

这正是 Master 和 Worker 在 Pod 调度策略上的区别所在，通常来说 Pod 都不能容忍任何“污点”，所以加上了 taint 属性的 Master 节点也就会无缘 Pod 了

让 DaemonSet 在 Master 节点（或者任意其他节点）上运行了，方法有两种：

一、 去掉 Master 节点上的 taint

```

kubectl taint node master node-role.kubernetes.io/master:NoSchedule-
```

二、 为 Pod 添加字段 tolerations，让它能够“容忍”某些“污点”，就可以在任意的节点上运行

tolerations 是一个数组，里面可以列出多个被“容忍”的“污点”，需要写清楚“污点”的名字、效果。比较特别是要用 operator 字段指定如何匹配“污点”，一般我们都使用 Exists，也就是说存在这个名字和效果的“污点”。如果我们想让 DaemonSet 里的 Pod 能够在 Master 节点上运行，就要写出这样的一个 tolerations，容忍节点的 node-role.kubernetes.io/master:NoSchedule 这个污点：

```
tolerations:
- key: node-role.kubernetes.io/master
  effect: NoSchedule
  operator: Exists
```



## 静态pod

DaemonSet 是在 Kubernetes 里运行节点专属 Pod 最常用的方式，但它不是唯一的方式，Kubernetes 还支持另外一种叫“静态 Pod”的应用部署手段。

“静态 Pod”非常特殊，它不受 Kubernetes 系统的管控，不与 apiserver、scheduler 发生关系，所以是“静态”的。但既然它是 Pod，也必然会“跑”在容器运行时上，也会有 YAML 文件来描述它，而唯一能够管理它的 Kubernetes 组件也就只有在每个节点上运行的 kubelet 了。

“静态 Pod”的 YAML 文件默认都存放在节点的 /etc/kubernetes/manifests 目录下，它是 Kubernetes 的专用目录。