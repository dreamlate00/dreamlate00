创建3台虚拟机

```
abcMaster：192.168.0.100
abcNode1：192.168.0.115
abcNode2：192.168.0.135
```

统一修改root用户密码

```
sudo passwd root
```

修改sshd服务配置，允许xshell以root用户访问

```
# 使用root身份修改/etc/ssh/sshd_config文件，设置PermitRootLogin为yes
# 重启sshd服务
service ssh restart
```

关闭防火墙、虚拟交换分区、selinux

```
# 关闭防火墙
ufw disable
# 关闭虚拟交换（注释fstab中swap配置）
vim /etc/fstab
# 关闭selinux（未找到）
```

设置主机hosts

```
cat >> /etc/hosts << EOF
192.168.0.100 abcmaster
192.168.0.115 abcnode1
192.168.0.135 abcnode2
EOF
```

将桥接的IPV4流量传递到iptables链中

```
# 配置
cat >> /etc/sysctl.d/k8s.conf << EOF
net.bridge.bridge-nf-call-ip6tables = 1
net.bridge.bridge-nf-call-iptables = 1
EOF
# 生效
sysctl --system
```

### 安装docker

- 设置apt-get 国内源

- ```
  # 备份/etc/apt/sources.list
  cp /etc/apt/sources.list /etc/apt/sources.list.bak
  # 使用vim 替换/etc/apt/sources.list中的资源地址
  deb http://mirrors.aliyun.com/ubuntu/ focal main restricted universe multiverse
  deb-src http://mirrors.aliyun.com/ubuntu/ focal main restricted universe multiverse
  
  deb http://mirrors.aliyun.com/ubuntu/ focal-security main restricted universe multiverse
  deb-src http://mirrors.aliyun.com/ubuntu/ focal-security main restricted universe multiverse
  
  deb http://mirrors.aliyun.com/ubuntu/ focal-updates main restricted universe multiverse
  deb-src http://mirrors.aliyun.com/ubuntu/ focal-updates main restricted universe multiverse
  
  deb http://mirrors.aliyun.com/ubuntu/ focal-proposed main restricted universe multiverse
  deb-src http://mirrors.aliyun.com/ubuntu/ focal-proposed main restricted universe multiverse
  
  deb http://mirrors.aliyun.com/ubuntu/ focal-backports main restricted universe multiverse
  deb-src http://mirrors.aliyun.com/ubuntu/ focal-backports main restricted universe multiverse
  ## 更新
  apt-get update
  ```

- 安装docker

  ```
  apt-get install -y docker.io
  ```

- 设置docker国内镜像源

  ```
  # 配置
  tee /etc/docker/daemon.json <<-'EOF'
  {
    "registry-mirrors": ["https://9zc7lu9m.mirror.aliyuncs.com"]
  }
  EOF
  # 重启
  systemctl daemon-reload
  systemctl restart docker
  ```



### 安装k8s

配置k8s安装源，然后安装kubelet、kubeadm、kubectl

```
apt-get update && apt-get install -y apt-transport-https
curl https://mirrors.aliyun.com/kubernetes/apt/doc/apt-key.gpg | apt-key add - 
cat >> /etc/apt/sources.list.d/kubernetes.list << EOF
deb https://mirrors.aliyun.com/kubernetes/apt/ kubernetes-xenial main
EOF  
apt-get update
apt-get install -y kubelet kubeadm kubectl
```

- 在master节点执行kubeadm初始化命令

```
kubeadm init --pod-network-cidr=10.244.0.0/16 --ignore-preflight-errors=NumCPU --apiserver-advertise-address=192.168.0.100 --image-repository registry.aliyuncs.com/google_containers
```



如果出错，修改docker服务的启动方式配置

```
# vim /usr/lib/systemd/system/docker.service
ExecStart=/usr/bin/dockerd -H fd:// --containerd=/run/containerd/containerd.sock --exec-opt native.cgroupdriver=systemd
# 重启docker 服务
systemctl daemon-reload && systemctl restart docker
```



按照提示执行后续命令

```
mkdir -p $HOME/.kube
cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
chown $(id -u):$(id -g) $HOME/.kube/config
export KUBECONFIG=/etc/kubernetes/admin.conf

```



查看部署状态

```
kubectl get nodes
# 执行结果
NAME        STATUS     ROLES                  AGE     VERSION
abcmaster   NotReady   control-plane,master   3m59s   v1.22.1
```

- 在abcnode1/2执行`kubeadm join`

```
kubeadm join 192.168.0.100:6443 --token 7ni2ey.qkjhtp3ygsn0lswk \
        --discovery-token-ca-cert-hash sha256:2ed9136ae664f9c74083f174e748be747c7e2926bdcf05877da003bd44f7fcc1
```



在abcmaster安装kube-flannel.yml插件

```
# 直接拉取并安装
kubectl apply -f https://raw.githubusercontent.com/coreos/flannel/master/Documentation/kube-flannel.yml
```

操作失败，无法连接到对象地址，则改为直接从gitHub下载kube-flannel.yml文件文件

```
kubectl apply -f ./kube-flannel.yml
```



查看pods状态

```
kubectl get pods -n kube-system
```



查看pod日志

```
kubectl logs kube-flannel-ds-amd64-m5w5w -n kube-system
```



参考：[ubuntu-20.04.3 使用kubeadm方式安装kubernetes(k8s)集群_arsiya_jerry的博客-CSDN博客](https://blog.csdn.net/arsiya_jerry/article/details/120322207)

[使用 kubeadm 部署 kubernetes(CRI 使用 containerd) · Docker -- 从入门到实践 (docker-practice.github.io)](https://docker-practice.github.io/zh-cn/kubernetes/setup/kubeadm.html)

