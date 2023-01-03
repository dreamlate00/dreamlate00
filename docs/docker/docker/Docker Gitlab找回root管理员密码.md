当忘记gitlab的默认管理员root的密码

或者我用docker创建gitlab第一次登陆时没有弹出root用户初始化界面时，按照以下方法可以重置管理员密码

进入docker 容器

```
docker exec -it gitlab（容器名字） /bin/bash
```

启用docker里面gitlab的ruby

```
gitlab-rails console -e production
```

找到管理员用户

```
user = User.where(id: 1).first
```

更改密码

```
user.password = 'abcd1234'
user.password_confirmation = 'abcd1234'
```

记得保存

```
user.save!
```