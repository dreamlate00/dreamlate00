使用ssky-keygen和ssh-copy-id

```
# 创建密钥信息
ssh-keygen -t  rsa
# 复制到192.168.234.192
 ssh-copy-id -i ~/.ssh/id_rsa.pub  root@192.168.234.192
 # 测试ssh免密登录
 ssh 'root@192.168.234.193'
```

