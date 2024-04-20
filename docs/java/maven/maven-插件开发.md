开发一个简单的maven



技术栈

- java

- maven

  

pom.xml

```
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>plugins</groupId>
  <artifactId>xyz</artifactId>
  <version>1.0.1</version>
  <packaging>maven-plugin</packaging>

  <name>xyz Maven Plugin</name>

  <!-- FIXME change it to the project's website -->
  <url>http://maven.apache.org</url>

  <properties>
    <java.version>1.8</java.version>
    <resource.delimiter>@</resource.delimiter>
    <maven.compiler.source>${java.version}</maven.compiler.source>
    <maven.compiler.target>${java.version}</maven.compiler.target>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
  </properties>

  <dependencies>
    <dependency>
      <groupId>org.apache.maven</groupId>
      <artifactId>maven-plugin-api</artifactId>
      <version>2.0</version>
    </dependency>
    <dependency>
      <groupId>org.apache.maven.plugin-tools</groupId>
      <artifactId>maven-plugin-annotations</artifactId>
      <version>3.2</version>
      <scope>provided</scope>
    </dependency>
    <dependency>
      <groupId>org.codehaus.plexus</groupId>
      <artifactId>plexus-utils</artifactId>
      <version>3.0.8</version>
    </dependency>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.8.2</version>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <build>
    <plugins>
      <plugin>
        <artifactId>maven-plugin-plugin</artifactId>
        <version>3.6.0</version>
        <configuration>
          <goalPrefix>xyz</goalPrefix>
          <skipErrorNoDescriptorsFound>true</skipErrorNoDescriptorsFound>
        </configuration>
        <executions>
          <execution>
            <id>mojo-descriptor</id>
            <goals>
              <goal>descriptor</goal>
            </goals>
          </execution>
          <execution>
            <id>help-goal</id>
            <goals>
              <goal>helpmojo</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>
</project>

```

打印文件树

```
package plugins;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

@Mojo(name = "tree")
public class TreeMojo extends AbstractMojo {
    /**
     * Location of the file.
     */
    @Parameter(defaultValue = "${project.basedir}", property = "dir", required = false)
    private File basedir;

    void tree(StringBuilder stringBuilder,File file, int level){

        String flag = "";
        if(level > 1){
            flag = new String(new char[level - 1]).replace("\0", "  ");
        }

        if(file.isDirectory()){
            if(level == 0){
                stringBuilder.append(file.getAbsolutePath()).append("\n");
            }else{
                stringBuilder.append(flag + "├─"+file.getName()).append("\n");
            }

            File[] files = file.listFiles();

            Arrays.sort(Objects.requireNonNull(files), (f1, f2) -> {
                if (f1.isDirectory() && !f2.isDirectory()) {
                    return -1;
                } else if (!f1.isDirectory() && f2.isDirectory()) {
                    return 1;
                } else {
                    return f1.compareTo(f2);
                }
            });

            for(File temp:files){
                tree(stringBuilder,temp,level+1);
            }

        }else {
            stringBuilder.append(flag + "│ "+file.getName()).append("\n");
        }
    }

    @Override
    public void execute() throws MojoExecutionException {

        StringBuilder builder = new StringBuilder();
        tree(builder,basedir,0);
        System.out.println(builder.toString());

    }
}

```





使用Mojo注解来标识是一个插件的入口

用Parameter来接收参数