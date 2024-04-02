查看文件

写入文件

追加文件

清空文件



写入文件

```
cat << EOF > nginx-ns.yaml
apiVersion: v1
kind: Namespace
metadata:
  name: nginx-ss
EOF
```



### cat << EOF

#### 1. 用途

从标准输入(stdin) 读取一段文本，遇到 "EOF" 就停止读取，然后将文本输出到标准输出(stdout) 中。

#### 2. 语法

```bash
cat << EOF
文本信息
EOF
```

**关键语法说明：**

**<<**：Linux 中的 Here Document 格式语法开始标识符；

**EOF**：一个标识符，标识文本信息的开始和结束，**可以是任意自定义字符，比如 begin，data 等**。

**文本信息**：用户（你）想要显示在终端的内容。

#### 3. '<< EOF' 和 '<<- EOF' 区别

> **If the redirection operator is <<-, then all leading tab characters are stripped from input lines and the line containing delimiter.**

说人话就是，**如果重定向操作符是 <<-，那么就忽略每行文本信息和结束标识符（比如上面的 EOF）中的前导制表符（tab）**。

```bash
#!/bin/sh

#line 1、2、3，EOF，data 1、2、3 前面为 tab，不是空格。
cat <<- EOF
        line 1
        line 2
        line 3
        EOF

cat << DATA
        data 1
        data 2
        data 3
DATA
```

**执行结果：**

![img](https://pic4.zhimg.com/80/v2-639d161e81e3a8db9eaf94acb1ab3c8f_720w.webp)

你看，使用 **'<<- '** 输出的内容**会忽略每行前面的 tab**。

#### 4. 延伸

如果想从终端中输入多行文本到文件中，应当如何做呢？

```bash
cat > file << EOF
information 1
information 1
information 1
...
EOF
```

![img](https://pic2.zhimg.com/80/v2-6faea6aff155de54a89f91056f41050d_720w.webp)
