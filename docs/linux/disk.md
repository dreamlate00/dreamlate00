```
lsblk 
lsblk -f

挂载磁盘:
1) 先添加scis磁盘
2) 添加分区,命令:fdisk /dev/sbd
	m:显示命令列表
	p:显示磁盘分区,同fdisk - l
	n:新增分区
	d:删除分区
	w:写入并保存
3) 格式化分区,命令:mkfs -t ext4 /dev/sdb1
4) 挂载 mount /dev/sdb1 /newdisk
5) 协助 unmount /dev/sdb1
6) 命令行挂载,重启后会丢失挂载关系
7) 永久挂载:通过修改/etc/fstab实现挂载,添加完成后,执行mount -a 立即生效

查询磁盘使用情况:
df -h
du -h /home
   -s:指定目录占用总大小
   -h:带计量单位
   -a:含文件
   --max-depth=1 子目录深度
   -c:列出明细的同时,增加汇总值
   
统计/opt文件夹下的文件的个数:
ls -l /opt |grep "^-" |wc -l
统计/opt文件夹下的目录的个数:
ls -l /opt |grep "^d" |wc -l
统计/opt文件夹下的文件的个数,包括子文件里:
ls -lR /opt |grep "^-" |wc -l
统计/opt文件夹下的目录的个数,包括子文件里:
ls -lR /opt |grep "^d" |wc -l
以树状结构显示:
tree /home
```

