关闭服务

```
ps -ef | grep personalNetworkingverification.jar | grep -v grep | awk '{print $2}'| xargs kill -9
# java程序
jps | grep 'your_application_name' | awk '{print $1}' | xargs kill -9
```

