```
kubectl run ngx --image=nginx:alpine
```



```
kubectl run ngx --image=nginx:alpine --dry-run=client -o yaml
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
apiVersion: v1
kind: Pod
metadata:
  name: busy-pod
  labels:
    owner: chrono
    env: demo
    region: north
    tier: back
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



```
kubectl apply -f busy-pod.yml
kubectl delete -f busy-pod.yml
```



```
kubectl delete pod busy-pod
```



```
kubectl logs busy-pod
```



```
kubectl describe busy-pod
```



```
kubectl describe pod busy-pod
```



```
kubectl get pod
```



```
kubectl exec -it ngx-pod -- sh
```

