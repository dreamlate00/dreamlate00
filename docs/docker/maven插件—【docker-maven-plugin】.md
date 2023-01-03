前置设置

设置环境变量DOCKER_HOST

```
DOCKER_HOST=tcp://<host>:2375
```

docker-maven-plugin 插件默认连接本地 Docker 地址为：localhost:2375，所以我们需要先设置下环境变量。

注意：如果没有设置 `DOCKER_HOST` 环境变量，可以在配置文件的config配置中指定 `DOCKER_HOST` 来执行。



构建镜像

指定构建信息到 POM 中构建

```
<build>
    <plugins>
        <plugin>
            <groupId>com.spotify</groupId>
            <artifactId>docker-maven-plugin</artifactId>
            <version>1.0.0</version>
            <configuration>
                <imageName>mavendemo</imageName>
                <baseImage>java</baseImage>
                <maintainer>docker_maven docker_maven@email.com</maintainer>
                <workdir>/ROOT</workdir>
                <cmd>["java", "-version"]</cmd>
                <entryPoint>["java", "-jar", "${project.build.finalName}.jar"]</entryPoint>
                <!-- 这里是复制 jar 包到 docker 容器指定目录配置 -->
                <resources>
                    <resource>
                        <targetPath>/ROOT</targetPath>
                        <directory>${project.build.directory}</directory>
                        <include>${project.build.finalName}.jar</include>
                    </resource>
                </resources>
            </configuration>
        </plugin>
    </plugins>
</build>
```

使用 Dockerfile 构建，dockerfile必须置于pom.xml同目录的位置

```
pom.xml配置
<build>
    <plugins>
         <plugin>
            <groupId>com.spotify</groupId>
            <artifactId>docker-maven-plugin</artifactId>
            <version>1.0.0</version>
            <configuration>
                <imageName>mavendemo</imageName>
                <dockerDirectory>${basedir}/docker</dockerDirectory> <!-- 指定 Dockerfile 路径-->
                <!-- 这里是复制 jar 包到 docker 容器指定目录配置，也可以写到 Docokerfile 中 -->
                <resources>
                    <resource>
                        <targetPath>/ROOT</targetPath>
                        <directory>${project.build.directory}</directory>
                        <include>${project.build.finalName}.jar</include>
                    </resource>
                </resources>
            </configuration>
        </plugin>   
    </plugins>
</build>

${basedir}/docker/Dockerfile 配置

FROM java
MAINTAINER docker_maven docker_maven@email.com
WORKDIR /ROOT
CMD ["java", "-version"]
ENTRYPOINT ["java", "-jar", "${project.build.finalName}.jar"]
```

执行

```
docker:build
mvn clean package docker:build 只执行 build 操作
mvn clean package docker:build -DpushImage 执行 build 完成后 push 镜像
mvn clean package docker:build -DpushImageTag 执行 build 并 push 指定 tag 的镜像 
```

绑定Docker 命令到 Maven 各个阶段

```
<build>
    <plugins>
        <plugin>
            <groupId>com.spotify</groupId>
            <artifactId>docker-maven-plugin</artifactId>
            <version>1.0.0</version>
            <configuration>
                <imageName>mavendemo</imageName>
                <baseImage>java</baseImage>
                <maintainer>docker_maven docker_maven@email.com</maintainer>
                <workdir>/ROOT</workdir>
                <cmd>["java", "-version"]</cmd>
                <entryPoint>["java", "-jar", "${project.build.finalName}.jar"]</entryPoint>
                <resources>
                    <resource>
                        <targetPath>/ROOT</targetPath>
                        <directory>${project.build.directory}</directory>
                        <include>${project.build.finalName}.jar</include>
                    </resource>
                </resources>
            </configuration>
            <executions>
                <execution>
                    <id>build-image</id>
                    <phase>package</phase>
                    <goals>
                        <goal>build</goal>
                    </goals>
                </execution>
                <execution>
                    <id>tag-image</id>
                    <phase>package</phase>
                    <goals>
                        <goal>tag</goal>
                    </goals>
                    <configuration>
                        <image>mavendemo:latest</image>
                        <newName>docker.io/wanyang3/mavendemo:${project.version}</newName>
                    </configuration>
                </execution>
                <execution>
                    <id>push-image</id>
                    <phase>deploy</phase>
                    <goals>
                        <goal>push</goal>
                    </goals>
                    <configuration>
                        <imageName>docker.io/wanyang3/mavendemo:${project.version}</imageName>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```



私有仓库

1 修改 POM 文件 imageName 

```
<configuration>
    <imageName>registry.example.com/wanyang3/mavendemo:v1.0.0</imageName>
    ...
</configuration>
```

2 修改 POM 文件中 newName

```
...
<configuration>
    <imageName>registry.example.com/wanyang3/mavendemo:v1.0.0</imageName>
    ...
</configuration>
<execution>
    <id>tag-image</id>
    <phase>package</phase>
    <goals>
        <goal>tag</goal>
    </goals>
    <configuration>
        <image>mavendemo</image>
        <newName>registry.example.com/wanyang3/mavendemo:v1.0.0</newName>
    </configuration>
</execution>
...
```

全认证配置

> 在settings.xml文件按里面添加，然后就可以使用serverId来获取账号信息

```
<servers>
  <server>
    <id>my-docker-registry</id>
    <username>wanyang3</username>
    <password>12345678</password>
    <configuration>
      <email>wanyang3@mail.com</email>
    </configuration>
  </server>
</servers>
```

```
<plugin>
  <plugin>
    <groupId>com.spotify</groupId>
    <artifactId>docker-maven-plugin</artifactId>
    <version>1.0.0</version>
    <configuration>
      <imageName>registry.example.com/wanyang3/mavendemo:v1.0.0</imageName>
      ...
      <serverId>my-docker-registry</serverId>
    </configuration>
  </plugin>
</plugins>
```

