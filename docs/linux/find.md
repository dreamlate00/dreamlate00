```
find /opt/soft/log/ -mtime +30 -name "*.log" -exec rm -rf {} \;
```

find 对应目录 -mtime +天数 -name "文件名" -exec rm -rf {} \;

```
说明：
将/opt/soft/log/目录下所有30天前带".log"的文件删除。具体参数说明如下：
find：linux的查找命令，用户查找指定条件的文件；
/opt/soft/log/：想要进行清理的任意目录；
-mtime：标准语句写法；
+30：查找30天前的文件，这里用数字代表天数；
"*.log"：希望查找的数据类型，"*.jpg"表示查找扩展名为jpg的所有文件，"*"表示查找所有文件，这个可以灵活运用，举一反三；
-exec：固定写法；
rm -rf：强制删除文件，包括目录；
{} \; ：固定写法，一对大括号+空格+\+; 
```



```
crontab -e
10 2 * * * /opt/soft/log/auto-del-7-days-ago-log.sh >/dev/null 2>&1
```



日志文件大于40M,清空

```
for i in $(find /cache/ -size +40M);
do
   echo " " > $i;
done
```



脚本文件

```
find ~/ -mtime +30 -name "*info*.log" -exec rm -rf {} \;
find ~/ -mtime +30 -name "*error*.log" -exec rm -rf {} \;
for i in $(find ~/lib*/logs/ -size +40M);
do
   echo " " > $i;
done
```

设置定时任务

```
进入配置
crontab -e
配置
10 2 * * *  脚本文件绝对路径 >/dev/null 2>&1
```



清理docker日志

```bash
#!/bin/sh

echo "======== docker containers logs file size ========"  
logs=$(find /var/lib/docker/containers/ -name *-json.log)  

for log in $logs  
        do  
             ls -lh $log   
        done  

查看docker日志

#!/bin/sh 
echo "======== start clean docker containers logs ========"  
logs=$(find /var/lib/docker/containers/ -name *-json.log)  
for log in $logs  
        do  
                echo "clean logs : $log"  
                cat /dev/null > $log  
        done  

echo "======== end clean docker containers logs ========"  

```

