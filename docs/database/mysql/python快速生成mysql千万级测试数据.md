背景
在开发过程中，我们会做性能测试，但有些性能测试只有数据量达到一定程度的才能更好的测试出来。为此我们需要特意研究如何快速的生成千万级乃至亿级别的数据。

数据生成思路
利用python生成千万级的txt文本数据
将txt文本数据通过mysql的 load data infile语句导入到数据库中，这中方式非常高效，快到一千万的数据两分钟搞定，当然这取决于你每行的数据量。
接下来就可以享受海量数据的体验啦。
生成千万级txt文本
在别的教程中，有用存储过程、临时内存表的方式，实现快速生成数据。我个人认为有以下不足

生成数据真实性不高
生成数据关联灵活性不高
如果利用python脚本，我们可以非常灵活的通过脚本代码来实现生成数据之间的关系与分布，而且我们可以利用pyton的一个开源的faker库，来生成各种看着不那么假的名字、地址、手机号、邮件等。git地址:faker

目标
我们模拟一个微博动态的一个过程，其中包含用户表，用户关注表，以及微博动态表。我们需要分析在一个用户有上百万的粉丝，微博动态的查询优化。

生成五百万用户数据
本来想用faker模拟真实数据，但是生成性能太低了，节约时间就暂时随意一点，空了会写个基于numpy的高性能随机

创建用户表

```
CREATE TABLE `user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NULL,
  `email` varchar(255) NULL,
  `phone` varchar(20) NULL,
  PRIMARY KEY (`id`)
);
```

python脚本生成(耗时66s)

```
import os
from datetime import datetime


def build_data(file_path, data_count):
    if os.path.exists(file_path):
        os.remove(file_path)

    file = open(file_path, 'w')
    start = datetime.now()
    print('数据生成中')
    for var in range(data_count):
        file.writelines(str(var)+" 用户"+str(var) + " email"+str(var)+"@email.com"+" "+"1592837482"+str(var))
        file.write('\n')
    end = datetime.now()
    print('耗时:'+str((end-start).seconds)+"s")
    
    
build_data('user.txt',5000000)

```

大概花费一分钟，大小200+M，可以利用多线程提高速度,这里id也要写进去

user.txt导入mysql(耗时40s)

```
load data infile '/var/lib/mysql/user.txt' into table user fields terminated by ' ';
在mysql命令行中执行，大概花费40s，全部导入数据。其中的路径是修改根据你的条件修改，分隔符是空格
```



生成五千万用户关系数据
现在我们假定平均下来每个用户有10个粉丝。但是某些小概率的用户有超过10个粉丝。这样的需求有点类似于微信抢红包算法。
我们来想象以下，有500w用户来瓜分5000w的红包，并且我们期望的均值是10，同时需要保证一个合理的数据离散程度即方差。但是这个问题困扰了我两天，最终也没能完美的解决。按照我预想的选择正态分布或者对数正态分布能够生成较好看的数据。但是还是无法解决，以后再考虑吧。需要学习的太多，不能偏离主线太多。

创建用户关系表

```
CREATE TABLE `user_follow` (
 `id` int(11) NOT NULL AUTO_INCREMENT,
 `user_id` int(11) DEFAULT NULL,
 `follow_user_id` int(11) DEFAULT NULL,
 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```


python脚本生成(耗时3分钟)
上面说的生成太复杂了。我们用最简单的一个用户对应10个固定id的关注。

```
# 生成千万个用户数据
import os
from datetime import datetime
import numpy as np
import threading
import math
# 用户量
user_count = 5000000
# 线程数
batch_size = 10
cell_length = math.ceil(user_count/batch_size)
file_path = 'user-follow.txt'
# 加载写入文件
if os.path.exists(file_path):
   os.remove(file_path)
file = open(file_path, 'w')
lock = threading.Lock()
from tqdm import tqdm

# 处理数据生成
def handler_gen(i,b):
    a =  i*cell_length
    b =  (i+1)*cell_length
    print(str(a)+","+str(b)+" start")
    for var in tqdm(range(a,b)):
        tmp=[1,100,200,300,400,500,600,700,800,900]
        for t in tmp:
            file.writelines(str(var+1)+" "+str(t))
            file.write('\n')

start = datetime.now()
print('数据生成中，线程数:'+str(batch_size))
threads = []
for i in range(batch_size):
    t = threading.Thread(target=handler_gen,args=(i,1))
    t.start()
    threads.append(t)
for t in threads:
    t.join()

end = datetime.now()
file.close()
print('耗时:'+str((end-start).seconds)+"s")
```


user-follow.txt导入mysql(耗时5m12s)

```
load data infile '/var/lib/mysql/user-follow.txt' ignore into table user_follow fields terminated by ' '(user_id,follow_user_id)set id = null;
```


1
应该是几分钟吧，我导入的时候navcat超时了。上面的sql自动忽略错误以及自增长id。

load data infile '/var/lib/mysql/user-follow.txt' ignore into table user_follow fields terminated by ' '(user_id,follow_user_id)set id = null;
Query OK, 50000000 rows affected, 40869 warnings (5 min 12.10 sec)
Records: 50000000  Deleted: 0  Skipped: 0  Warnings: 40869
好了，用了几天时间终于整理完了。主要那个正态分布研究很久，虽然没在这里用，还是很有收获。因为如果加上那些py生成的速度会慢很多。

下一章将研究这些海量数据在查询上的性能问题。
————————————————
版权声明：本文为CSDN博主「mt23」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/mathcoder23/article/details/103544929