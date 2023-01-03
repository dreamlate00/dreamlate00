configMap

```

export out="--dry-run=client -o yaml"        # 定义Shell变量
kubectl create cm info $out
kubectl create cm info --dry-run=client -o yaml
kubectl create cm info --from-literal=k=v --dry-run=client -o yaml
```



Create a config map based on a file, directory, or specified literal value.

 A single config map may package one or more key/value pairs.

 When creating a config map based on a file, the key will default to the basename of the file, and the value will
default to the file content.  If the basename is an invalid key, you may specify an alternate key.

 When creating a config map based on a directory, each file whose basename is a valid key in the directory will be
packaged into the config map.  Any directory entries except regular files are ignored (e.g. subdirectories, symlinks,
devices, pipes, etc).



```
Examples:
  # Create a new config map named my-config based on folder bar
  kubectl create configmap my-config --from-file=path/to/bar

  # Create a new config map named my-config with specified keys instead of file basenames on disk
  kubectl create configmap my-config --from-file=key1=/path/to/bar/file1.txt --from-file=key2=/path/to/bar/file2.txt

  # Create a new config map named my-config with key1=config1 and key2=config2
  kubectl create configmap my-config --from-literal=key1=config1 --from-literal=key2=config2

  # Create a new config map named my-config from the key=value pairs in the file
  kubectl create configmap my-config --from-file=path/to/bar

  # Create a new config map named my-config from an env file
  kubectl create configmap my-config --from-env-file=path/to/foo.env --from-env-file=path/to/bar.env
```

整个文件夹作为参数，文件名是key

```
kubectl create cm app-cfg --from-file=/home/app/k8s/configMap/
```

使用envfile

```
app@minikube0:~/k8s$ kubectl create cm env0 --from-file=/home/app/k8s/env-file --dry-run=client -o yaml
apiVersion: v1
data:
  env-file: |
    LANG=zh_CN.UTF-8
kind: ConfigMap
metadata:
  creationTimestamp: null
  name: env0
```



```
apiVersion: v1
kind: ConfigMap
metadata:
  name: info

data:
  count: '10'
  debug: 'on'
  path: '/etc/systemd'
  greeting: |
    say hello to kubernetes.
```





## Secret

同configMap，只是value使用base64转译

```

echo -n "123456" | base64
MTIzNDU2

secret.yml
apiVersion: v1
kind: Secret
metadata:
  name: user

data:
  name: cm9vdA==  # root
  pwd: MTIzNDU2   # 123456
  db: bXlzcWw=    # mysql

```



```
kubectl apply  -f secret.yml
kubectl get secret
kubectl describe secret user
```

