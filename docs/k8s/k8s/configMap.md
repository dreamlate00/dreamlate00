```
export out="--dry-run=client -o yaml"        # 定义Shell变量
kubectl create cm info $out
kubectl create cm info --from-literal=k=v $out
kubectl get cm
kubectl describe cm info
```





ConfigMap 是一种 API 对象，用来将非机密性的数据保存到键值对中。使用时， [Pod](https://kubernetes.io/zh-cn/docs/concepts/workloads/pods/) 可以将其用作环境变量、命令行参数或者存储卷中的配置文件。

ConfigMap 将你的环境配置信息和[容器镜像](https://kubernetes.io/zh-cn/docs/reference/glossary/?all=true#term-image)解耦，便于应用配置的修改。

使用 ConfigMap 可以实现配置数据和应用程序代码分离，减少耦合。

> ConfigMap 在设计上不是用来保存大量数据的。在 ConfigMap 中保存的数据不可超过 1 MiB。如果你需要保存超出此尺寸限制的数据，你可能希望考虑挂载存储卷 或者使用独立的数据库或者文件服务。



## ConfigMap 对象

ConfigMap 是一个让你可以存储其他对象所需要使用的配置的 [API 对象](https://kubernetes.io/zh-cn/docs/concepts/overview/working-with-objects/#kubernetes-objects)。 和其他 Kubernetes 对象都有一个 `spec` 不同的是，ConfigMap 使用 `data` 和 `binaryData` 字段。这些字段能够接收键-值对作为其取值。`data` 和 `binaryData` 字段都是可选的。`data` 字段设计用来保存 UTF-8 字符串，而 `binaryData` 则被设计用来保存二进制数据作为 base64 编码的字串。

ConfigMap 的名字必须是一个合法的 [DNS 子域名](https://kubernetes.io/zh-cn/docs/concepts/overview/working-with-objects/names#dns-subdomain-names)。

`data` 或 `binaryData` 字段下面的每个键的名称都必须由字母数字字符或者 `-`、`_` 或 `.` 组成。在 `data` 下保存的键名不可以与在 `binaryData` 下出现的键名有重叠。

从 v1.19 开始，你可以添加一个 `immutable` 字段到 ConfigMap 定义中， 创建[不可变更的 ConfigMap](https://kubernetes.io/zh-cn/docs/concepts/configuration/configmap/#configmap-immutable)。



## 创建ConfigMap

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: game-demo
data:
  # 类属性键；每一个键都映射到一个简单的值
  player_initial_lives: "3"
  ui_properties_file_name: "user-interface.properties"

  # 类文件键
  game.properties: |
    enemy.types=aliens,monsters
    player.maximum-lives=5    
  user-interface.properties: |
    color.good=purple
    color.bad=yellow
    allow.textmode=true    
```



可以使用四种方式来使用 ConfigMap 配置 Pod 中的容器：

1. 在容器命令和参数内
2. 容器的环境变量
3. 在只读卷里面添加一个文件，让应用来读取
4. 编写代码在 Pod 中运行，使用 Kubernetes API 来读取 ConfigMap





```
apiVersion: v1
kind: Pod
metadata:
  name: configmap-demo-pod
spec:
  containers:
    - name: demo
      image: alpine
      command: ["sleep", "3600"]
      env:
        # 定义环境变量
        - name: PLAYER_INITIAL_LIVES # 请注意这里和 ConfigMap 中的键名是不一样的
          valueFrom:
            configMapKeyRef:
              name: game-demo           # 这个值来自 ConfigMap
              key: player_initial_lives # 需要取值的键
        - name: UI_PROPERTIES_FILE_NAME
          valueFrom:
            configMapKeyRef:
              name: game-demo
              key: ui_properties_file_name
      volumeMounts:
      - name: config
        mountPath: "/config"
        readOnly: true
  volumes:
  # 你可以在 Pod 级别设置卷，然后将其挂载到 Pod 内的容器中
  - name: config
    configMap:
      # 提供你想要挂载的 ConfigMap 的名字
      name: game-demo
      # 来自 ConfigMap 的一组键，将被创建为文件
      items:
      - key: "game.properties"
        path: "game.properties"
      - key: "user-interface.properties"
        path: "user-interface.properties"
```



## 使用 ConfigMap

ConfigMap 可以作为数据卷挂载。ConfigMap 也可被系统的其他组件使用， 而不一定直接暴露给 Pod。例如，ConfigMap 可以保存系统中其他组件要使用的配置数据。

ConfigMap 最常见的用法是为同一命名空间里某 Pod 中运行的容器执行配置。 你也可以单独使用 ConfigMap。

比如，你可能会遇到基于 ConfigMap 来调整其行为的 [插件](https://kubernetes.io/zh-cn/docs/concepts/cluster-administration/addons/) 或者 [operator](https://kubernetes.io/zh-cn/docs/concepts/extend-kubernetes/operator/)。

### 在 Pod 中将 ConfigMap 当做文件使用

要在一个 Pod 的存储卷中使用 ConfigMap:

1. 创建一个 ConfigMap 对象或者使用现有的 ConfigMap 对象。多个 Pod 可以引用同一个 ConfigMap。
2. 修改 Pod 定义，在 `spec.volumes[]` 下添加一个卷。 为该卷设置任意名称，之后将 `spec.volumes[].configMap.name` 字段设置为对你的 ConfigMap 对象的引用。
3. 为每个需要该 ConfigMap 的容器添加一个 `.spec.containers[].volumeMounts[]`。 设置 `.spec.containers[].volumeMounts[].readOnly=true` 并将 `.spec.containers[].volumeMounts[].mountPath` 设置为一个未使用的目录名， ConfigMap 的内容将出现在该目录中。
4. 更改你的镜像或者命令行，以便程序能够从该目录中查找文件。ConfigMap 中的每个 `data` 键会变成 `mountPath` 下面的一个文件名。



下面是一个将 ConfigMap 以卷的形式进行挂载的 Pod 示例：

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: mypod
spec:
  containers:
  - name: mypod
    image: redis
    volumeMounts:
    - name: foo
      mountPath: "/etc/foo"
      readOnly: true
  volumes:
  - name: foo
    configMap:
      name: myconfigmap
```

你希望使用的每个 ConfigMap 都需要在 `spec.volumes` 中被引用到。

如果 Pod 中有多个容器，则每个容器都需要自己的 `volumeMounts` 块，但针对每个 ConfigMap，你只需要设置一个 `spec.volumes` 块。



#### 被挂载的 ConfigMap 内容会被自动更新

当卷中使用的 ConfigMap 被更新时，所投射的键最终也会被更新。 kubelet 组件会在每次周期性同步时检查所挂载的 ConfigMap 是否为最新。 不过，kubelet 使用的是其本地的高速缓存来获得 ConfigMap 的当前值。 高速缓存的类型可以通过 [KubeletConfiguration 结构](https://kubernetes.io/zh-cn/docs/reference/config-api/kubelet-config.v1beta1/). 的 `configMapAndSecretChangeDetectionStrategy` 字段来配置。

ConfigMap 既可以通过 watch 操作实现内容传播（默认形式），也可实现基于 TTL 的缓存，还可以直接经过所有请求重定向到 API 服务器。 因此，从 ConfigMap 被更新的那一刻算起，到新的主键被投射到 Pod 中去， 这一时间跨度可能与 kubelet 的同步周期加上高速缓存的传播延迟相等。 这里的传播延迟取决于所选的高速缓存类型 （分别对应 watch 操作的传播延迟、高速缓存的 TTL 时长或者 0）。

以环境变量方式使用的 ConfigMap 数据不会被自动更新。 更新这些数据需要重新启动 Pod。

**说明：** 使用 ConfigMap 作为 [subPath](https://kubernetes.io/zh-cn/docs/concepts/storage/volumes#using-subpath) 卷挂载的容器将不会收到 ConfigMap 的更新。

## 不可变更的 ConfigMap

**特性状态：** `Kubernetes v1.21 [stable]`

Kubernetes 特性 *Immutable Secret 和 ConfigMaps* 提供了一种将各个 Secret 和 ConfigMap 设置为不可变更的选项。对于大量使用 ConfigMap 的集群 （至少有数万个各不相同的 ConfigMap 给 Pod 挂载）而言，禁止更改 ConfigMap 的数据有以下好处：

- 保护应用，使之免受意外（不想要的）更新所带来的负面影响。
- 通过大幅降低对 kube-apiserver 的压力提升集群性能， 这是因为系统会关闭对已标记为不可变更的 ConfigMap 的监视操作。

你可以通过将 `immutable` 字段设置为 `true` 创建不可变更的 ConfigMap。 例如：

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  ...
data:
  ...
immutable: true
```

一旦某 ConfigMap 被标记为不可变更，则 *无法* 逆转这一变化，，也无法更改 `data` 或 `binaryData` 字段的内容。你只能删除并重建 ConfigMap。 因为现有的 Pod 会维护一个已被删除的 ConfigMap 的挂载点，建议重新创建这些 Pods