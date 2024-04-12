集成jacoco做覆盖测试

pom.xml

```xml
    <properties>
    	...
        <jacoco-maven-plugin.version>0.8.6</jacoco-maven-plugin.version>
        <maven-surefire-plugin.version>2.22.2</maven-surefire-plugin.version>
    </properties>
    
    <build>
        <plugins>
            <plugin>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>${maven-surefire-plugin.version}</version>
            </plugin>
            <plugin>
                <groupId>org.jacoco</groupId>
                <artifactId>jacoco-maven-plugin</artifactId>
                <version>${jacoco-maven-plugin.version}</version>
                <executions>
                    <execution>
                        <id>pre-test</id>
                        <goals>
                            <goal>prepare-agent</goal>
                        </goals>
                    </execution>
                    <execution>
                        <id>post-test</id>
                        <phase>test</phase>
                        <goals>
                            <goal>report</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
```

