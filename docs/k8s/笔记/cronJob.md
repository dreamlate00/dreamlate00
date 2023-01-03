```

export out="--dry-run=client -o yaml"              # 定义Shell变量
kubectl create cj echo-cj --image=busybox --schedule="" --dry-run=client -o yaml
```



```
app@minikube0:~/k8s$ kubectl create cj echo-cj --image=busybox --schedule="" --dry-run=client -o yaml
apiVersion: batch/v1
kind: CronJob
metadata:
  creationTimestamp: null
  name: echo-cj
spec:
  jobTemplate:
    metadata:
      creationTimestamp: null
      name: echo-cj
    spec:
      template:
        metadata:
          creationTimestamp: null
        spec:
          containers:
          - image: busybox
            name: echo-cj
            resources: {}
          restartPolicy: OnFailure
  schedule: ""
status: {}
```



```
apiVersion: batch/v1
kind: CronJob
metadata:
  creationTimestamp: null
  name: echo-cj
spec:
  schedule: "*/1 * * * *"
  jobTemplate:
    metadata:
      creationTimestamp: 202212201605
      name: echo-cj
    spec:
      template:
        metadata:
          creationTimestamp: 202212201605
        spec:
          restartPolicy: OnFailure
          containers:
          - image: busybox
            name: echo-cj
            imagePullPolicy: IfNotPresent 
            command: ["/bin/echo"] 
            args: ["hello", "world"]
  
```



```
apiVersion: batch/v1
kind: CronJob
metadata:
  name: echo-cj
spec:
  schedule: "*/1 * * * *"
  jobTemplate:
    metadata:
      creationTimestamp: 202212201605
      name: echo-cj
    spec:
      template:
        metadata:
          creationTimestamp: 202212201605
        spec:
          restartPolicy: OnFailure
          containers:
          - image: busybox
            name: echo-cj
            imagePullPolicy: IfNotPresent 
            command: ["/bin/echo"] 
            args: ["`date '+%Y-%m-%d %H:%M:%S'`"]
```



```
apiVersion: batch/v1
kind: CronJob
metadata:
  name: hello
spec:
  schedule: "* * * * *"
  jobTemplate:
    spec:
      template:
        spec:
          containers:
          - name: hello
            image: busybox:1.28
            imagePullPolicy: IfNotPresent
            command:
            - /bin/sh
            - -c
            - date; echo Hello from the Kubernetes cluster
          restartPolicy: OnFailure
```



```
app@minikube0:~/k8s$ kubectl get cj
NAME    SCHEDULE    SUSPEND   ACTIVE   LAST SCHEDULE   AGE
hello   * * * * *   False     0        <none>          24s
app@minikube0:~/k8s$ kubectl get pod -w
NAME                   READY   STATUS      RESTARTS      AGE
echo-job-5nvgk         0/1     Completed   0             19m
hello-27858735-fr9bk   0/1     Completed   0             91s
hello-27858736-78r9k   0/1     Completed   0             31s
ngx                    1/1     Running     1 (98m ago)   23h
sleep-job-lmttl        0/1     Completed   0             15m
sleep-job-nn25l        0/1     Completed   0             15m
sleep-job-qhzl8        0/1     Completed   0             14m
sleep-job-v65mn        0/1     Completed   0             14m
app@minikube0:~/k8s$ kubectl logs hello-27858735-fr9bk
Tue Dec 20 08:15:10 UTC 2022
Hello from the Kubernetes cluster
app@minikube0:~/k8s$ kubectl logs hello-27858736-78r9k
Tue Dec 20 08:16:01 UTC 2022
```

