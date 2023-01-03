```

export out="--dry-run=client -o yaml"              # 定义Shell变量
kubectl create job echo-job --image=busybox --dry-run=client -o yaml
```



```
kubectl apply -f job.yml

kubectl get job
kubectl get pod

kubectl logs echo-job-5nvgk
```



```
app@minikube0:~/k8s$ kubectl get pod
NAME             READY   STATUS      RESTARTS      AGE
echo-job-5nvgk   0/1     Completed   0             3m46s
ngx              1/1     Running     1 (82m ago)   22h
app@minikube0:~/k8s$ kubectl logs echo-job-5nvgk
hello world
```





```
app@minikube0:~/k8s$ kubectl get job
NAME        COMPLETIONS   DURATION   AGE
echo-job    1/1           2s         4m59s
sleep-job   4/4           13s        14s
app@minikube0:~/k8s$
app@minikube0:~/k8s$ kubectl get pod -w
NAME              READY   STATUS      RESTARTS      AGE
echo-job-5nvgk    0/1     Completed   0             5m14s
ngx               1/1     Running     1 (83m ago)   22h
sleep-job-lmttl   0/1     Completed   0             29s
sleep-job-nn25l   0/1     Completed   0             29s
sleep-job-qhzl8   0/1     Completed   0             20s
sleep-job-v65mn   0/1     Completed   0             19s
app@minikube0:~/k8s$
app@minikube0:~/k8s$ kubectl logs sleep-job-lmttl
done
app@minikube0:~/k8s$ kubectl logs sleep-job-nn25l
done
app@minikube0:~/k8s$ kubectl logs sleep-job-qhzl8
done
app@minikube0:~/k8s$ kubectl logs sleep-job-v65mn
done
```

