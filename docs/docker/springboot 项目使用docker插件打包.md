最基础的springboot应用

1.application.yml

```
server:
  port: 7000
spring:
  application:
    name: spring-boot-docker-sample
```

2.Application.java

```java
@SpringBootApplication
@RestController
public class Application {
	@GetMapping(value = "hello")
    public Object sayHello(){
        return "hello! spring boot with docker.";
    }
    public static void main(String[] args){
        SpringApplication.run(Application.class, args);
    }
}
```

docker-maven-plugin插件

> 这个插件是基于dockerfile构造的镜像

```xml
<plugin>
    <groupId>io.fabric8</groupId>
    <artifactId>docker-maven-plugin</artifactId>
    <version>0.32.0</version>
    <executions>
        <execution>
            <id>build-image</id>
            <phase>package</phase>
            <goals>
                <goal>build</goal>
            </goals>
            <configuration>
                <!-- Docker 远程管理地址-->
                <dockerHost>http://192.168.49.142:2375</dockerHost>
                <!--认证配置,用于私有registry认证-->
                <authConfig>
                    <!--                                <username>docker</username>-->
                    <!--                                <password>docker</password>-->
                </authConfig>

                <images>
                    <image>
                        <!--由于推送到私有镜像仓库，镜像名需要添加仓库地址-->
                        <dockerHost>http://192.168.49.142:2375</dockerHost>
                        <name>192.168.49.142:5000/${project.name}:${project.version}</name>
                        <registry>192.168.49.142</registry>
                        <!--镜像build相关配置-->
                        <build>
                            <!--使用dockerFile文件-->
                            <dockerFile>${project.basedir}/Dockerfile</dockerFile>
                        </build>
                    </image>
                </images>
            </configuration>
        </execution>
    </executions>
</plugin>
```

dockerfile

```
FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY target/docker-sample-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

执行mvn package dockerfile:build构造镜像到本地仓库