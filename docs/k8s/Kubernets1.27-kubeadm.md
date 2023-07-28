规划

| IP              | HOSTNAME   | 系统      |
| --------------- | ---------- | --------- |
| 192.168.234.191 | k8s-master | Centos7.9 |
| 192.168.234.192 | k8s-node01 | Centos7.9 |
| 192.168.234.193 | k8s-node02 | Centos7.9 |



软件版本

| **名称**                  | **版本号** |
| ------------------------- | ---------- |
| kubeadm、kubelet、kubectl | 1.27.0     |
| containerd                | 1.6.21     |



```
cat <<EOF > /etc/hosts
127.0.0.1   localhost localhost.localdomain localhost4 localhost4.localdomain4
::1         localhost localhost.localdomain localhost6 localhost6.localdomain6
192.168.234.191 k8s-master-01
192.168.234.192 k8s-worker-01
192.168.234.193 k8s-worker-02
EOF
```



修改haostname

```
hostnamectl set-hostname k8s-master && bash
hostnamectl set-hostname k8s-node01 && bash
hostnamectl set-hostname k8s-node02 && bash
```



关闭防火墙、selinux

```
#关闭selinux
sed -i 's/^ *SELINUX=enforcing/SELINUX=disabled/g' /etc/selinux/config
#关闭防火墙
systemctl stop firewalld.service 
systemctl disable firewalld
```



添加yum源

```
[root@k8s-master ~]# mv /etc/yum.repos.d/CentOS-Base.repo /etc/yum.repos.d/CentOS-Base.repo.bak
[root@k8s-master ~]# wget -O /etc/yum.repos.d/CentOS-Base.repo http://mirrors.aliyun.com/repo/Centos-7.repo
```



添加k8s源

```
[root@k8s-master ~]# cat <<EOF > /etc/yum.repos.d/kubernetes.repo
[kubernetes]
name=Kubernetes
baseurl=https://mirrors.aliyun.com/kubernetes/yum/repos/kubernetes-el7-x86_64
enabled=1
gpgcheck=0
repo_gpgcheck=0
gpgkey=https://mirrors.aliyun.com/kubernetes/yum/doc/yum-key.gpg https://mirrors.aliyun.com/kubernetes/yum/doc/rpm-package-key.gpg
EOF
```



重置yum源

```
yum clean all
yum makecache
```



同步时间

```
timedatectl set-timezone Asia/Shanghai
# 安装ntpdate
yum install ntpdate -y
# 同步时间
ntpdate ntp1.aliyun.com
#可设置定时任务定时同步时间

```



关闭交换分区

```
sed -ri 's/.*swap.*/#&/' /etc/fstab 
swapoff -a
```



永久添加模块

```
vim /etc/modules-load.d/k8s.conf
overlay
br_netfilter
```



手动添加

```
modprobe br_netfilter
modprobe overlay
```



查看添加效果

```
lsmod | grep br_netfilter
```



修改内核参数

```
#配置 k8s 网络配置
cat > /etc/sysctl.d/k8s.conf <<EOF
net.bridge.bridge-nf-call-ip6tables = 1
net.bridge.bridge-nf-call-iptables = 1
net.ipv4.ip_forward = 1
EOF


#这个命令的作用是应用 k8s.conf 文件中的内核参数设置，并且开启网络桥接的防火墙功能。其中 k8s.conf 文件中的内容包括以下三个参数设置：
#net.bridge.bridge-nf-call-iptables = 1 表示开启防火墙功能。
#net.bridge.bridge-nf-call-ip6tables = 1 表示开启 IPV6 的防火墙功能。
#net.ipv4.ip_forward = 1 表示开启 IP 转发功能。
sysctl -p /etc/sysctl.d/k8s.conf

#重新加载内核参数配置文件，以确保这些设置生效。
sysctl --system
```



安装ipvs模块

```
yum -y install ipset ipvsadm
vim /etc/sysconfig/modules/ipvs.modules

modprobe -- ip_vs
modprobe -- ip_vs_rr
modprobe -- ip_vs_wrr
modprobe -- ip_vs_sh
modprobe -- nf_conntrack

chmod 755 /etc/sysconfig/modules/ipvs.modules && bash /etc/sysconfig/modules/ipvs.modules && lsmod | grep -e ip_vs -e nf_conntrack
```



安装基础包

```
yum install -y yum-utils device-mapper-persistent-data lvm2 wget net-tools nfs-utils lrzsz gcc gcc-c++ make cmake libxml2-devel openssl-devel curl curl-devel unzip sudo ntp libaio-devel wget vim ncurses-devel autoconf automake zlibdevel python-devel epel-release openssh-server socat ipvsadm conntrack ntpdate telnet ipvsadm
```





Containerd

```
yum install containerd -y
systemctl start containerd && systemctl enable containerd
```

配置修改

```
mkdir -p /etc/containerd
containerd config default > /etc/containerd/config.toml

mkdir -p /etc/containerd && \
containerd config default > /etc/containerd/config.toml && \
sed -i "s#k8s.gcr.io/pause#registry.aliyuncs.com/google_containers/pause#g"  /etc/containerd/config.toml  && \
sed -i 's#SystemdCgroup = false#SystemdCgroup = true#g' /etc/containerd/config.toml  && \
sed -i 's#registry.k8s.io/pause:3.6#registry.aliyuncs.com/k8sxio/pause:3.6#g' /etc/containerd/config.toml  && \
sed -i '/registry.mirrors]/a\ \ \ \ \ \ \ \ [plugins."io.containerd.grpc.v1.cri".registry.mirrors."docker.io"]' /etc/containerd/config.toml  && \
sed -i '/registry.mirrors."docker.io"]/a\ \ \ \ \ \ \ \ \ \ endpoint = ["http://hub-mirror.c.163.com"]' /etc/containerd/config.toml && \
sed -i '/hub-mirror.c.163.com"]/a\ \ \ \ \ \ \ \ [plugins."io.containerd.grpc.v1.cri".registry.mirrors."k8s.gcr.io"]' /etc/containerd/config.toml  && \
sed -i '/"k8s.gcr.io"]/a\ \ \ \ \ \ \ \ \ \ endpoint = ["http://registry.aliyuncs.com/google_containers"]' /etc/containerd/config.toml && \
echo "===========restart containerd to reload config===========" && \
systemctl restart containerd

# crictl脚本的配置
cat <<EOF > /etc/crictl.yaml
runtime-endpoint: unix:///run/containerd/containerd.sock
image-endpoint: unix:///run/containerd/containerd.sock
timeout: 10
debug: false
EOF
```



安装kubernetes

```
#查看可安装的kubernetes 的版本
yum list kubelet --showduplicates | sort -r

#安装
yum install -y kubelet-1.27.0 kubeadm-1.27.0 kubectl-1.27.0 --disableexcludes=kubernetes

#配置开机自启
systemctl enable --now kubelet

#Kubeadm: kubeadm 是一个工具，用来初始化 k8s 集群的
#kubelet: 安装在集群所有节点上，用于启动 Pod 的
#kubectl: 通过 kubectl 可以部署和管理应用，查看各种资源，创建、删除和更新各种组件

#查看版本
kubeadm version

#指定容器运行时为containerd
crictl config runtime-endpoint /run/containerd/containerd.sock
```



初始化k8s（主节点）

生成脚本

```
#生成文件
[root@k8s-master ~]# kubeadm config print init-defaults > kubeadm.yaml
[root@k8s-master ~]# vim kubeadm.yaml
apiVersion: kubeadm.k8s.io/v1beta3
bootstrapTokens:
- groups:
  - system:bootstrappers:kubeadm:default-node-token
  token: abcdef.0123456789abcdef
  ttl: 24h0m0s
  usages:
  - signing
  - authentication
kind: InitConfiguration
localAPIEndpoint:
  advertiseAddress: 192.168.2.40	#master节点ip
  bindPort: 6443
nodeRegistration:
  criSocket: unix:///var/run/containerd/containerd.sock
  imagePullPolicy: IfNotPresent
  name: k8s-master	#主节点名
  taints: null
---
apiServer:
  timeoutForControlPlane: 4m0s
apiVersion: kubeadm.k8s.io/v1beta3
certificatesDir: /etc/kubernetes/pki
clusterName: kubernetes
controllerManager: {}
dns: {}
etcd:
  local:
    dataDir: /var/lib/etcd
imageRepository: registry.cn-hangzhou.aliyuncs.com/google_containers	#替换镜像仓库
kind: ClusterConfiguration
kubernetesVersion: 1.27.0
networking:
  dnsDomain: cluster.local
  podSubnet: 10.244.0.0/16	#pod 网段
  serviceSubnet: 10.96.0.0/12
scheduler: {}
---		#新增
apiVersion: kubeproxy.config.k8s.io/v1alpha1
kind: KubeProxyConfiguration
mode: ipvs
---
apiVersion: kubelet.config.k8s.io/v1beta1
kind: KubeletConfiguration
cgroupDriver: systemd

也可以直接用
kubeadm init \
--apiserver-advertise-address=192.168.234.170 \
--control-plane-endpoint=k8s-master \
--image-repository registry.cn-hangzhou.aliyuncs.com/google_containers \
--kubernetes-version v1.20.9 \
--service-cidr=10.96.0.0/16 \
--pod-network-cidr=192.168.0.0/16
```

拉取镜像

```
kubeadm config images pull --config=kubeadm.yaml
```

初始化主节点

```
kubeadm init --config kubeadm.yaml | tee kubeadm-init.log
```

出现配置命令后即为成功



配置 kubectl 的配置文件 config，相当于对 kubectl 进行授权，这样 kubectl 命令可以使用这个证 书对 k8s 集群进行管理

```
mkdir -p $HOME/.kube
sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
sudo chown $(id -u):$(id -g) $HOME/.kube/config
export KUBECONFIG=/etc/kubernetes/admin.conf
```



检查节点

```
kubectl get nodes
```



配置工作节点

```
#仅node节点上执行
[root@k8s-master ~]# mkdir -p $HOME/.kube

#同步.kube/config文件夹
[root@k8s-master ~]# xsync ~/.kube/config

#此命令为init之后打印在控制台中
#仅node节点执行
[root@k8s-node01 ~]# kubeadm join 192.168.2.40:6443 --token abcdef.0123456789abcdef \
        --discovery-token-ca-cert-hash sha256:09bdee3b5da241080291a2aed41939a801dc5932a2caedf90b1fac8831450acb

#执行后可能存在以下报错，因为存在cri-docker与containerd两个容器运行时
Found multiple CRI endpoints on the host. Please define which one do you wish to use by setting the 'criSocket' field in the kubeadm configuration file: unix:///var/run/containerd/containerd.sock, unix:///var/run/cri-dockerd.sock
To see the stack trace of this error execute with --v=5 or higher
#手动指定容器运行时为containerd，其他情况同理
[root@k8s-node01 ~]# kubeadm join 192.168.2.40:6443 --token abcdef.0123456789abcdef \
        --discovery-token-ca-cert-hash sha256:09bdee3b5da241080291a2aed41939a801dc5932a2caedf90b1fac8831450acb \
				--cri-socket=unix:///var/run/containerd/containerd.sock

#未记录可在主节点执行此命令重新打印
[root@k8s-master ~]# kubeadm token create --print-join-command
```



设置节点标签

```
#查看节点标签
[root@k8s-master ~]# kubectl get nodes --show-labels
NAME         STATUS     ROLES           AGE   VERSION   LABELS
k8s-master   NotReady   control-plane   53m   v1.27.0   beta.kubernetes.io/arch=amd64,beta.kubernetes.io/os=linux,kubernetes.io/arch=amd64,kubernetes.io/hostname=k8s-master,kubernetes.io/os=linux,node-role.kubernetes.io/control-plane=,node.kubernetes.io/exclude-from-external-load-balancers=
k8s-node01   NotReady   <none>          32m   v1.27.0   beta.kubernetes.io/arch=amd64,beta.kubernetes.io/os=linux,kubernetes.io/arch=amd64,kubernetes.io/hostname=k8s-node01,kubernetes.io/os=linux
k8s-node02   NotReady   <none>          38m   v1.27.0   beta.kubernetes.io/arch=amd64,beta.kubernetes.io/os=linux,kubernetes.io/arch=amd64,kubernetes.io/hostname=k8s-node02,kubernetes.io/os=linux
k8s-node03   NotReady   <none>          38m   v1.27.0   beta.kubernetes.io/arch=amd64,beta.kubernetes.io/os=linux,kubernetes.io/arch=amd64,kubernetes.io/hostname=k8s-node03,kubernetes.io/os=linux

#设置标签
[root@k8s-master ~]# kubectl label nodes k8s-node01 node-role.kubernetes.io/work=work
[root@k8s-master ~]# kubectl label nodes k8s-node02 node-role.kubernetes.io/work=work
```



网络插件

Calico（首选）

```
curl https://docs.projectcalico.org/manifests/calico.yaml -O
kubectl apply -f calico.yaml   # 部署 calico 网络插件
```

flannel（次选）

```
#方法1：下载flannel
[root@k8s-master ~]# curl -O https://raw.githubusercontent.com/coreos/flannel/master/Documentation/kube-flannel.yml
#安装
[root@k8s-master ~]# kubectl apply -f kube-flannel.yml
#方法2：很有可能国内网络访问不到这个资源，你可以网上找找国内的源安装 flannel
[root@k8s-master ~]# kubectl apply -f https://raw.githubusercontent.com/coreos/flannel/master/Documentation/kube-flannel.yml
```

Weave

```
#如果上面的插件安装失败，可以选用 Weave，下面的命令二选一就可以了。
[root@k8s-master ~]# kubectl apply -f https://github.com/weaveworks/weave/releases/download/v2.8.1/weave-daemonset-k8s.yaml
[root@k8s-master ~]# kubectl apply -f http://static.corecore.cn/weave.v2.8.1.yaml
```

kubectl get node显示**STATUS**都为Ready即安装完成