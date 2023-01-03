minikube in action!

Start a cluster by running:

```shell
minikube start
```



Access the Kubernetes dashboard running within the minikube cluster:

```shell
minikube dashboard
```

Once started, you can interact with your cluster using `kubectl`, just like any other Kubernetes cluster. For instance, starting a server:

```shell
kubectl create deployment hello-minikube --image=k8s.gcr.io/echoserver:1.4
```

Exposing a service as a NodePort

```shell
kubectl expose deployment hello-minikube --type=NodePort --port=8080
```

minikub makes it easy to open this exposed endpoint in your browser:

```shell
minikube service hello-minikube
```

Upgrade your cluster:

```shell
minikube start --kubernetes-version=latest
```

Start a second local cluster (*note: This will not work if minikube is using the bare-metal/none driver*):

```shell
minikube start -p cluster2
```

Stop your local cluster:

```shell
minikube stop
```

Delete your local cluster:

```shell
minikube delete
```

Delete all local clusters and profiles

```shell
minikube delete --all
```