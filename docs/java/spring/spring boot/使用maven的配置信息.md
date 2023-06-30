application.yml

```
spring:
    application:
        name: @artifactId@
    profiles:
        active: @profiles.active@
```



```
<artifactId>payment</artifactId>
```



```
<profiles>
   <profile>
      <id>dev</id>
      <properties>
         <!-- 环境标识，需要与配置文件的名称相对应 -->
         <profiles.active>dev</profiles.active>
      </properties>
      <activation>
         <!-- 默认环境 -->
         <activeByDefault>true</activeByDefault>
      </activation>
   </profile>
   <profile>
      <id>test</id>
      <properties>
         <profiles.active>test</profiles.active>
      </properties>
   </profile>
   <profile>
      <id>prod</id>
      <properties>
         <profiles.active>prod</profiles.active>
      </properties>
   </profile>
</profiles>
```

