### 一、宿主机与容器的挂载

docker可以支持把一个宿主机上的目录挂载到镜像里。命令如下：

```
docker run -it -v /mydownload:/download nginx:v1 /bin/bash
```

通过-v参数，冒号前为宿主机目录，必须为绝对路径，冒号后为镜像内挂载的路径。

现在镜像内就可以共享宿主机里的文件了。

默认挂载的路径权限为读写。如果指定为只读可以用：ro, 之后在容器内进行文件的操作，将报出以下错误

```
$ touch text.txt
touch: cannot touch 'text.txt': Read-only file system
```

### 二、数据卷容器

如果你有一些持续更新的数据需要在容器之间共享，最好创建数据卷容器。
数据卷容器，其实就是一个正常的容器，专门用来提供数据卷供其它容器挂载的。

首先，创建一个普通的数据卷容器。用--name给他指定了一个名（不指定的话会生成一个随机的名子）。

```
docker run -it -v /mydownload/:/download --name dataVol nginx:v1 /bin/bash
```

再创建一个新的容器，来使用这个数据卷。

```
docker run -it --volumes-from dataVol nginx:latest /bin/bash
```

--volumes-from用来指定要从哪个数据卷来挂载数据。

可以看到每个容器中都有挂载目录/download

```
$ docker run -it --volumes-from dataVol nginx:latest /bin/bash
$ ls
bin  boot  dev    download  etc  home  lib  lib64  media    mnt  opt  proc    root  run  sbin  srv  sys  tmp    usr  var
```

 

### 三、容器内外互相拷贝数据

####   1、从容器内拷贝文件到主机上

```
docker cp <containerId>:<containerDir>  <localDir>  
```

- <containerId>:<containerDir> 表示容器id加具体路径，
- <localDir>   表示主机路径

 2、从主机上拷贝到容器（用卷挂载或直接拷贝）
 这里将直接拷贝的方式
 第一步：获取容器的完整ID：FULL_CONTAINER_ID

```
docker inspect -f   '{{.Id}}' {CONTAINER ID}
或
docker inspect -f  '{{.Id}}'  {CONTAINER NAME}
```

 第二步：从主机上拷贝到容器

```
sudo cp file.txt /var/lib/docker/aufs/mnt/**FULL_CONTAINER_ID/PATH-NEW-FILE    
```



```

Usage:  docker cp [OPTIONS] CONTAINER:SRC_PATH DEST_PATH|-
        docker cp [OPTIONS] SRC_PATH|- CONTAINER:DEST_PATH

Copy files/folders between a container and the local filesystem

Use '-' as the source to read a tar archive from stdin
and extract it to a directory destination in a container.
Use '-' as the destination to stream a tar archive of a
container source to stdout.

Options:
  -a, --archive       Archive mode (copy all uid/gid information)
  -L, --follow-link   Always follow symbol link in SRC_PATH

```

