```
<plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-antrun-plugin</artifactId>
        <executions>
          <execution>
            <phase>package</phase>
            <goals>
              <goal>run</goal>
            </goals>
            <configuration>
              <target>
                <copy file="${project.basedir}/target/${project.build.finalName}.${project.packaging}"
                  tofile="${project.build.outputDirectory}/docker/app.jar"/>
              </target>
            </configuration>
          </execution>
        </executions>
      </plugin>
      <plugin>
        <groupId>com.spotify</groupId>
        <artifactId>dockerfile-maven-plugin</artifactId>
        <version>1.4.10</version>
        <configuration>
          <!--设置镜像仓库的账户及密码，没有该部分可删除-->
          <!--<username></username>-->
          <!--<password>$password$</password>-->
          <!--设置镜像仓库地址，格式为 仓库地址/镜像所属用户/镜像名称-->
          <repository>
            ${docker.repository}/${docker.namespace}/${docker.image.name}
          </repository>
          <!--设置镜像tag-->
          <tag>${docker.image.tag}</tag>
          <!--设置Docker上下文，即DockerFile的位置-->
          <contextDirectory>${project.build.outputDirectory}/docker</contextDirectory>
        </configuration>
        <dependencies>
          <dependency>
            <groupId>javax.activation</groupId>
            <artifactId>activation</artifactId>
            <version>1.1</version>
          </dependency>
        </dependencies>
      </plugin>
```



dockerfile

```
#指定从哪个镜像开始
FROM adoptopenjdk/openjdk11:alpine-slim
#声明作者
LABEL maintainer=fanjunxiang<fanjx@jinyundt.com>
#设置镜像挂载的数据卷，项目中所有文件路径可以设置在此数据卷中，如日志保存位置等
VOLUME /opt/data
#设置镜像时间区域，避免出现时区不一致现象
RUN echo "Asia/Shanghai" > /etc/timezone && \
    mkdir -p /opt/data && \
    mkdir -p /opt/bin
ENV ADDITIONAL_EUREKA_SERVER_LIST test
ENV ACM_ENDPOINT test
ENV ACM_NAMESPACE test
ENV ACM_GROUP test
ENV JAVA_TOOL_OPTIONS="-XX:+UseContainerSupport -Dfile.encoding=UTF-8"
#暴露端口
EXPOSE 10010
#拷贝jar文件至镜像中
COPY app.jar  /opt/bin/app.jar
#设置执行命令
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/opt/bin/app.jar"]
```

