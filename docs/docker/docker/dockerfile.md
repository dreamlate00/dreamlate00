最佳实践

```dockerfile
# 镜像
FROM mysql:8.0.19
# 创建者
MAINTAINER 13751880511@163.com
# 环境变量 - 时区
ENV TZ=Asia/Shanghai
# 时区处理
RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
# 要初始化执行的脚本
COPY ./tables_nacos.sql /docker-entrypoint-initdb.d
COPY ./jeecgboot-mysql-5.7.sql /docker-entrypoint-initdb.d
COPY ./tables_xxl_job.sql /docker-entrypoint-initdb.d
```

