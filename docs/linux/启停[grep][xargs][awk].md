关闭服务

```
ps -ef | grep personalNetworkingverification.jar | grep -v grep | awk '{print $2}'| xargs kill -9
```

