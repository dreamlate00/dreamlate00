```bash
cat  > /etc/hosts << EOF
127.0.0.1   localhost localhost.localdomain localhost4 localhost4.localdomain4
::1         localhost localhost.localdomain localhost6 localhost6.localdomain6
172.16.10.110 k8s-master
172.16.10.111 k8s-node1
172.16.10.112 k8s-node2
EOF
```

