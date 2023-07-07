```
# 找出CPU占用高的线程
top 
# 线程列表，找到CPU最高的那个
ps -mp $pid -o THREAD,tid,time
# 将线程ID转成16进制
prinrtf "%x\n" $tid
jstack $PID | grep $TID -A60 >> errror_$PID_$TID
```

