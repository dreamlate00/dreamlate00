关键点
排查Kubernetes部署故障的3个步骤：

1. 应确保Pods正常运行；
2. 确保于服务可以将流量调度到Pod；
3. 检查是否正确配置了入口



Pod故障排查
在大多数情况下，问题出在Pod本身。应该确保Pod正在运行并准备就绪（READY为1）。
检查方法：

```
kubectl get pods
```

关键点
可以用下面几个命令用来排查Pod故障：

```
#用来查看Pod容器日志。
kubectl logs 
# 用于查看与Pod相关的事件列表。
kubectl describe pod 
# 用于获取Pod的YAML定义。
kubectl get pod 
# 对进入Pod容器进行交互式终端。
kubectl exec -ti bash
```

常见Pod错误列表
Pod可能会出现各种启动和运行时错误。

启动错误：
**ImagePullBackoff**，**ImageInspectError**，**ErrImagePull**，**ErrImageNeverPull**，**RegistryUnavailable**，**InvalidImageName**

运行时错误：
**CrashLoopBackOff**，**RunContainerError**，**KillContainerError**，**VerifyNonRootError**，**RunInitContainerError**，**CreatePodSandboxError**，**ConfigPodSandboxError**，**KillPodSandboxError**，**SetupNetworkError**，**TeardownNetworkError**

关键错误代码及其修复方法
ImagePullBackOff
当Kubernetes无法检索Pod容器之一的图像时，将出现此错误。
主要三个原因：
1）镜像名称无效。例如，输错名字，或者镜像不存在。
2）为镜像指定了一个不存在的标签。
3）尝试检索的镜像属于一个私有注册表，但是Kubernetes没有设置权限访问。
解决方法：
前两种情况可以通过修改镜像名和标签来解决。
第三个问题，需要在注册表中添加凭据，并在Pod中引用。
官方文档中有一个有关如何实现此目标的示例。

CrashLoopBackOff
如果容器无法启动，则Kubernetes status会显示CrashLoopBackOff错误。
通常，Pod在以下情况下容器无法启动：
应用程序中出现错误，阻止其启动；
未正确配置容器；
Liveness探针失败太多次；
解决方法：
应该查看容器中日志，了解详细失败的原因。
kubectl logs --previous

RunContainerError
当容器无法启动时出现错误，直至在容器内的应用程序启动之前。
该问题通常是由于配置错误，例如：

挂载不存在的卷，例如ConfigMap或Secrets
将只读卷安装为可读写
解决方法：
对该错误应该使用kubectl describe pod 来收集和分析错误。

Pod处于待处理状态
当创建Pod时，该Pod保持在待处理状态。主要可能原因：
群集没有足够的资源（例如CPU和内存）来运行Pod；
当前的命名空间具有ResourceQuota对象，创建Pod将使命名空间超过配额；
Pod绑定到一个待处理的PersistentVolumeClaim；
解决方法：
检查kubectl describe命令的事件部分：
kubectl describe pod
对于因ResourceQuotas而导致的错误，可以使用以下方法检查群集的日志：
kubectl get events --sort-by=.metadata.creationTimestamp

Pod处于未就绪状态
如果Pod正在运行但未就绪，则表示"就绪"探针失败。
当就绪探针失败时，Pod未连接到服务，并且不会有流量转发到该实例。
解决方法
准备就绪探针失败是特定于应用程序的错误，因此应该检查kubectl描述中的"事件"部分以识别错误。

服务故障排查
如果的Pod正在运行且已就绪，但仍无法收到应用程序的响应，则应检查服务的配置是否正确。

关键点
服务的主要功能是根据流量的标签将流量路由到Pod。所以，先应该检查服务定位了多少个Pod，可以通过检查服务中的端点来查看：
kubectl describe service | grep Endpoints
端点是一对<ip address：port>，并且在服务（至少）以Pod为目标时，应该至少有一个。
如果"端点"部分为空，则有两种原因：

没有运行带有正确标签的Pod，应检查是否在正确的命名空间。

服务的选择器标签中有错字；

如果可以看到端点列表，但仍然无法访问应用程序，则很大原因是服务中的targetPort配置有误。

可以通过使用kubectl port-forward连接到服务具体排查：

kubectl port-forward service/ 3000:80

入口故障排查

如果Pod运行正常，服务可以分配流量到Pod，则可能原因是入口配置有误：

根据入口可能使用不同控制器类型，需要按具体对应方法进行调试。

关键点

检查入口配置参数serviceName和servicePort配置是否正确。可以使用下面命令检查：

kubectl describe ingress

如果"后端"列为空，则配置中肯定有一个错误。

如果可以在"后端"列中看到端口，但是仍然无法访问该应用程序，则可能是以下问题：

没有如何将入口发布到公网；没有如何将群集发布到公网；

可以通过直接连接到Ingress Pod来将基础结构问题与入口隔离开。

首先，查看入口控制器Pod列表：
kubectl get pods --all-namespaces
其次，使用kubectl describe命令查看端口：
kubectl describe pod nginx-ingress-controller-6fc5bcc
最后，连接到Pod：
kubectl port-forward nginx-ingress-controller-6fc5bcc 3000:80 --namespace kube-system
这样，访问计算机上的端口3000时，请求都会转发到Pod上的端口80。现在应用可以用吗？
如果可行，则问题出在基础架构中。应该检查如何将流量调度到群集。
如果还不行，则问题出在入口控制器中。应该调试入口控制器。常见的入口控制包括Nginx，HAProxy，Traefik等，可以查看具体控制器相关文档进行问题排查。此处我们以Nginx为例：
排查Nginx控制器
Ingress-nginx项目是Kubectl官方插件。可以使用kubectl ingress-nginx执行以下操作：
查看日志，后端，证书等；
连接到入口；
检查当前配置。
对应的命令有：
kubectl ingress-nginx lint：用于检查nginx.conf
kubectl ingress-nginx backend：用于检查后端（类似于kubectl describe ingress ）
kubectl ingress-nginx logs：查看控制器日志。

总结
对Kubernetes集群的故障排查应该遵循从下至上排查方法：先从Pod开始，然后是服务和入口，依次按顺序排查。
————————————————
版权声明：本文为CSDN博主「富贵007」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/weixin_44782815/article/details/109952231