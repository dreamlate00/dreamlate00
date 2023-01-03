```
 <plugin>
        <!-- 插件配置仓库地址 https://github.com/GoogleContainerTools/jib/tree/master/jib-maven-plugin -->
        <groupId>com.google.cloud.tools</groupId>
        <artifactId>jib-maven-plugin</artifactId>
        <version>1.0.2</version>
        <configuration>
          <!-- 选择初始镜像 -->
          <from>
            <image>jundemon/google-distroless-java:11</image>
            <!-- 初始镜像镜像所在仓库如果需要登录此处设置账户密码 -->
<!--            <auth>-->
<!--              <username></username>-->
<!--              <password></password>-->
<!--            </auth>-->
          </from>
          <!-- 设置推送的镜像名称，需包括仓库地址 -->
          <to>
            <image>registry.cn-hangzhou.aliyuncs.com/demonjun/demo</image>
            <!-- 目标镜像镜像所在仓库如果需要登录此处设置账户密码 -->
<!--            <auth>-->
<!--              <username></username>-->
<!--              <password></password>-->
<!--            </auth>-->
            <!-- 目标镜像镜像的tag，默认包含 latest -->
            <tags>
              <tag>dev</tag>
            </tags>
          </to>
          <container>
            <!-- 设置容器运行时jvm参数 -->
            <jvmFlags>
              <jvmFlag>-Djava.security.egd=file:/dev/./urandom</jvmFlag>
              <jvmFlag>-XX:+UseContainerSupport</jvmFlag>
              <jvmFlag>-Dfile.encoding=UTF-8</jvmFlag>
              <jvmFlag>-Duser.timezone=Asia/ShangHai</jvmFlag>
            </jvmFlags>
            <!-- 设置main方法所在的类名称 -->
            <mainClass>com.demon.demo.DemoApplication</mainClass>
            <!-- 设置容器的开放的端口，等同于Dockerfile中的 EXPOSE -->
            <ports>
              <port>8080</port>
            </ports>
            <!-- 设置容器需要的环境变量，等同于Dockerfile中的ENV -->
            <environment>
              <env>dev</env>
              <!-- ..... -->
            </environment>
            <!-- 设置容器需要的标签，等同于Dockerfile中的LABEL -->
<!--            <labels>-->
<!--              <key1>value1</key1>-->
<!--            </labels>-->
            <!-- 设置容器的数据卷，等同于Dockerfile中的 VOLUME -->
            <volumes>
              <volume>/opt/data</volume>
            </volumes>
            <useCurrentTimestamp>true</useCurrentTimestamp>
          </container>
          <!-- 私有非HTTPS仓库需添加该配置 -->
          <allowInsecureRegistries>true</allowInsecureRegistries>
        </configuration>
      </plugin>
```

