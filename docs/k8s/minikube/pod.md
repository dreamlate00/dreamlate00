```
kubectl run ngx --image=nginx:alpine
```



```
apiVersion: v1
kind: Pod
metadata:
  name: ngx-pod
  labels:
    env: demo
    owner: chrono

spec:
  containers:
  - image: nginx:alpine
    name: ngx
    ports:
    - containerPort: 80
```



```
kubectl apply -f ngx-pod.yml
kubectl delete -f ngx-pod.yml
```



```
kubectl run ngx --image=nginx:alpine --dry-run=client -o yaml
```



```
http://192.168.234.201:9000/api/v1/namespaces/default/pods/ngx
```



```

spec:
  containers:
  - image: busybox:latest
    name: busy
    imagePullPolicy: IfNotPresent
    env:
      - name: os
        value: "ubuntu"
      - name: debug
        value: "on"
    command:
      - /bin/echo
    args:
      - "$(os), $(debug)"
```

