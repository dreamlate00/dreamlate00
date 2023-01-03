1、首先创建Dckerfile：

```
FROM mysql:5.7
#设置免密登录
ENV MYSQL_ALLOW_EMPTY_PASSWORD yes
#将所需文件放到容器中
COPY setup.sh /mysql/setup.sh
COPY schema.sql /mysql/schema.sql
COPY privileges.sql /mysql/privileges.sql

#设置容器启动时执行的命令
CMD ["sh", "/mysql/setup.sh"]
```
2、编写容器启动脚本setup.sh：

```
#!/bin/bash
set -e

#查看mysql服务的状态，方便调试，这条语句可以删除
echo `service mysql status`

echo '1.启动mysql....'
#启动mysql
service mysql start
sleep 3
echo `service mysql status`

echo '2.开始导入数据....'
#导入数据
mysql < /mysql/schema.sql
echo '3.导入数据完毕....'

sleep 3
echo `service mysql status`

#重新设置mysql密码
echo '4.开始修改密码....'
mysql < /mysql/privileges.sql
echo '5.修改密码完毕....'

#sleep 3
echo `service mysql status`
echo `mysql容器启动完毕,且数据导入成功`

tail -f /dev/null
```

这里是先导入数据，然后才是设置用户和权限，是因为mysql容器一开始为免密登录，Dockerfile中有如下设置：`ENV MYSQL_ALLOW_EMPTY_PASSWORD yes`,此时执行导入数据命令不需要登录验证操作，如果是先执行权限操作，那么导入数据则需要登录验证，整个过程就麻烦了许多。

3、需要导入数据的mysql脚本命令schema.sql：

```
-- 创建数据库
create database `docker_mysql` default character set utf8 collate utf8_general_ci;

use docker_mysql;

-- 建表
DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL,
  `created_at` bigint(40) DEFAULT NULL,
  `last_modified` bigint(40) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 插入数据
INSERT INTO `user` (`id`, `created_at`, `last_modified`, `email`, `first_name`, `last_name`, `username`)
VALUES
    (0,1490257904,1490257904,'john.doe@example.com','John','Doe','user');
```

因为是测试，所以随便写了一个建表语句，如果是真实项目肯定不止这一张表，直接将建表语句覆盖过来就好。

4、mysql权限设置命令privileges.sql：

```
use mysql;
select host, user from user;
-- 因为mysql版本是5.7，因此新建用户为如下命令：
create user docker identified by '123456';
-- 将docker_mysql数据库的权限授权给创建的docker用户，密码为123456：
grant all on docker_mysql.* to docker@'%' identified by '123456' with grant option;
-- 这一条命令一定要有：
flush privileges;
```

5、创建镜像

```
docker build -t docker-mysql-init-data .
```

docker build 为创建镜像命令，名称为docker-mysql-init-data，'.'表示当前目录，即Dockerfile文件所在的目录

```
$ docker build -t docker-mysql-init-data .
Sending build context to Docker daemon  6.144kB
Step 1/6 : FROM mysql:5.7
5.7: Pulling from library/mysql
fc7181108d40: Already exists 
787a24c80112: Pull complete 
a08cb039d3cd: Pull complete 
4f7d35eb5394: Pull complete 
5aa21f895d95: Pull complete 
a742e211b7a2: Pull complete 
0163805ad937: Pull complete 
62d0ebcbfc71: Pull complete 
559856d01c93: Pull complete 
c849d5f46e83: Pull complete 
f114c210789a: Pull complete 
Digest: sha256:c3594c6528b31c6222ba426d836600abd45f554d078ef661d3c882604c70ad0a
Status: Downloaded newer image for mysql:5.7
 ---> a1aa4f76fab9
Step 2/6 : ENV MYSQL_ALLOW_EMPTY_PASSWORD yes
 ---> Running in 7ef903100274
Removing intermediate container 7ef903100274
 ---> e0b13ef4cdea
Step 3/6 : COPY setup.sh /mysql/setup.sh
 ---> e3e3d110e677
Step 4/6 : COPY schema.sql /mysql/schema.sql
 ---> a518ec11da67
Step 5/6 : COPY privileges.sql /mysql/privileges.sql
 ---> 3122063dfdd5
Step 6/6 : CMD ["sh", "/mysql/setup.sh"]
 ---> Running in 8f551037fa01
Removing intermediate container 8f551037fa01
 ---> 8fb5362648b9
Successfully built 8fb5362648b9
```

[![复制代码](imgs/copycode.gif)](javascript:void(0);)

 

 6、找到生成的镜像，启动容器

```
$ docker run -p 13306:3306 -d docker-mysql-init-data
```

查看日志

```
$ docker logs bc4lcbc9ansba
MySQL Community Server 5.7.26 is not running.
1.启动mysql....
2019-07-08T03:50:47.131210Z 0 [Warning] TIMESTAMP with implicit DEFAULT value is deprecated. Please use --explicit_defaults_for_timestamp server option (see documentation for more details).
2019-07-08T03:50:47.331141Z 0 [Warning] InnoDB: New log files created, LSN=45790
2019-07-08T03:50:47.355405Z 0 [Warning] InnoDB: Creating foreign key constraint system tables.
2019-07-08T03:50:47.414068Z 0 [Warning] No existing UUID has been found, so we assume that this is the first time that this server has been started. Generating a new UUID: 91ddb324-a133-11e9-9a7c-0242ac110002.
2019-07-08T03:50:47.415870Z 0 [Warning] Gtid table is not ready to be used. Table 'mysql.gtid_executed' cannot be opened.
2019-07-08T03:50:47.416972Z 1 [Warning] root@localhost is created with an empty password ! Please consider switching off the --initialize-insecure option.
..
MySQL Community Server 5.7.26 is started.
MySQL Community Server 5.7.26 is running.
2.开始导入数据....
3.导入数据完毕....
MySQL Community Server 5.7.26 is running.
4.开始修改密码....
host    user
localhost    mysql.session
localhost    mysql.sys
localhost    root
5.修改密码完毕....
MySQL Community Server 5.7.26 is running.
/mysql/setup.sh: 1: /mysql/setup.sh: mysql容器启动完毕,且数据导入成功: not found
```

 