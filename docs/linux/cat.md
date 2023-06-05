查看文件

写入文件

追加文件

清空文件

```
cat /dev/null > file
```

跨行写入

```
cat > /etc/mysql/conf.d/mysql-skip-name-resolv.cnf <<EOF
[mysqld]
skip_name_resolve
EOF
```

