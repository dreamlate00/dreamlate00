配置阿里云镜像源
目的是从国外下载镜像太慢或者下载不了
阿里云也有开源的镜像仓库

下面开始配置
配置阿里云镜像源，默认以后镜像都从阿里云拉取

第一步：
登陆阿里云控制台----搜索容器镜像服务–镜像加速器–获取加速器地址


第二步：
操作参考 [容器镜像服务 (aliyun.com)](https://cr.console.aliyun.com/cn-hangzhou/instances/mirrors)
现在运行docker pull下载就会很快

配置阿里云镜像仓库
（目的是你想拥有一个镜像仓库，类似harbor,把自己的镜像保存在里面）
登陆阿里云控制台----搜索容器镜像服务

创建命名空间


点击创建镜像仓库


创建过程中注意选择 本地仓库


创建完点击管理：获取操作指南
注意上面出现的tomcat是我自己定义的，在你上传过程中要换成自己对应的，比如mysql,redis

设置拉取镜像密码


打标签
把你想上传的镜像名称改成你镜像地址的路径名称

上传
上传成功
要拉取就把push换成pull

常用命令
登录阿里云Docker Registry
$ sudo docker login --username=lidexiang033039 registry.cn-shanghai.aliyuncs.com
用于登录的用户名为阿里云账号全名，密码为开通服务时设置的密码。
您可以在产品控制台首页修改登录密码。
从Registry中拉取镜像
$ sudo docker pull registry.cn-shanghai.aliyuncs.com/laiyifen-docker/tomcat:[镜像版本号]
将镜像推送到Registry
$ sudo docker login --username=lidexiang033039 registry.cn-shanghai.aliyuncs.com$ sudo docker tag [ImageId] registry.cn-shanghai.aliyuncs.com/laiyifen-docker/tomcat:[镜像版本号]$ sudo docker push registry.cn-shanghai.aliyuncs.com/laiyifen-docker/tomcat:[镜像版本号]
请根据实际镜像信息替换示例中的[ImageId]和[镜像版本号]参数。
选择合适的镜像仓库地址
从ECS推送镜像时，可以选择使用镜像仓库内网地址。推送速度将得到提升并且将不会损耗您的公网流量。
如果您使用的机器位于VPC网络，请使用 registry-vpc.cn-shanghai.aliyuncs.com 作为Registry的域名登录，并作为镜像命名空间前缀。