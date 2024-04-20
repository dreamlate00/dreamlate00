需求： git提交代码的时候使用默认模板，自己在上面加内容就好

指定模板文件

```
usage: 
git config          commit.template /path/to/commit_template.txt
git config --global commit.template /path/to/commit_template.txt
```

使用

```
git commit
```





还有一个更直接的办法，修改.git/conf文件

.git/conf

```
[commit]
	template = D:\\dev\\git\\commit_template.txt
```

