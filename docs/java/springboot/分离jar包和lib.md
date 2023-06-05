基于spring-boot-maven-plugin打包的包可以直接使用java -jar执行，缺点是jar包和依赖放在一起，体积太大。



为了分离jar包和依赖，并且方便部署，使用maven-jar-plugin+maven-dependency-plugin+maven-assembly-plugin实现jar包依赖分离，并且打成便于部署的tar包

```
addClasspath：表示需要加入到类构建路径
classpathPrefix：指定生成的Manifest文件中Class-Path依赖lib前面都加上该前缀路径，构建出lib/xx.jar
mainClass：表示项目的启动类。
```

```
<build>
   <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-jar-plugin</artifactId>
            <version>3.1.1</version>
            <configuration>
                <archive>
                	<!-- 生成的jar中不要包含pom.xml和pom.properties这两个文件 -->
                    <addMavenDescriptor>false</addMavenDescriptor>
                    <manifest>
                        <addClasspath>true</addClasspath>
                        <classpathPrefix>lib/</classpathPrefix>
                        <!--这里需要修改为你的项目的主启动类-->
                        <mainClass>com.cecjx.business.BusinessApplication</mainClass> 
                    </manifest>
                </archive>
            </configuration>
        </plugin>
    </plugins>
</build>
```

```
<build>
   <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-dependency-plugin</artifactId>
            <executions>
                <execution>
                    <id>copy-lib</id>
                    <phase>package</phase>
                    <goals>
                        <goal>copy-dependencies</goal>
                    </goals>
                    <configuration>
                        <outputDirectory>target/lib</outputDirectory>
                        <excludeTransitive>false</excludeTransitive>
                        <stripVersion>false</stripVersion>
                        <includeScope>runtime</includeScope>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

```
<build>
    <plugins>
        <plugin>
            <artifactId>maven-assembly-plugin</artifactId>
            <configuration>
                <!-- 打包后的包名是否包含assembly的id名 -->
                <appendAssemblyId>false</appendAssemblyId>
                <!-- 指定最后tar或者zip包的名字 -->
                <finalName>djys-business</finalName>
                <!-- tar或者zip包的输出目录 -->
                <outputDirectory>target/</outputDirectory>
                <descriptors>
                    <!-- 引用的assembly配置文件-->
                    <descriptor>src/main/resources/assembly.xml</descriptor>
                </descriptors>
            </configuration>
            <executions>
                <!-- phase加入package后，则在执行maven package时就可以调用maven-assembly-plugin插件定义的打包方式 -->
                <execution>
                    <!--名字任意 -->
                    <id>make-assembly</id>
                    <!-- 绑定到package生命周期阶段上 -->
                    <phase>package</phase>
                    <goals>
                        <!-- 只运行一次 -->
                        <goal>single</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

```
<assembly xmlns="http://maven.apache.org/ASSEMBLY/2.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/ASSEMBLY/2.0.0 http://maven.apache.org/xsd/assembly-2.0.0.xsd">

    <id>package</id>
    <formats>
        <format>zip</format>
    </formats>
    <includeBaseDirectory>true</includeBaseDirectory>
    <fileSets>
        <!--拷贝application.yml文件到jar包的外部config目录下面-->
        <fileSet>
            <directory>${basedir}/src/main/resources</directory>
            <includes>
                <include>*.yml</include>
            </includes>
            <filtered>true</filtered>
            <outputDirectory>${file.separator}config</outputDirectory>
        </fileSet>

		<!--拷贝lib包到jar包的外部lib下面-->
        <fileSet>
            <directory>${project.build.directory}/lib</directory>
            <outputDirectory>${file.separator}lib</outputDirectory>
            <!-- 打包需要包含的文件 -->
            <includes>
                <include>*.jar</include>
            </includes>
        </fileSet>

		<!--如有需要，可以配置多个需要拷贝的文件即可-->
        <fileSet>
            <directory>${project.build.directory}</directory>
            <outputDirectory>${file.separator}</outputDirectory>
            <includes>
                <include>*.jar</include>
            </includes>
        </fileSet>
    </fileSets>
</assembly>
```

