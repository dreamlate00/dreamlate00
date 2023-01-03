pom.xml

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>test</artifactId>
        <groupId>x.y.z</groupId>
        <version>1.0.0</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>mockitos</artifactId>

    <properties>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

application.properties

```
server.port=8082
# Configuration for MySQL Database
spring.jpa.hibernate.ddl-auto=update
spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
spring.datasource.url=jdbc:mysql://localhost:3306/test
spring.datasource.username=root
spring.datasource.password=admin
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.show-sql:true
```

主类

```
package x.y.z.mokitos;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MokitosApp {
    public static void main(String[] args) {
        SpringApplication.run(MokitosApp.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner1() {
        return (args -> {
            // app 启动后执行
        });
    }
}
```

实体

```
package x.y.z.mokitos.enties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Api {
    @Id
    private Integer id;
    private String method;
    private String uri;
}
```

Repo

```
package x.y.z.mokitos.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import x.y.z.mokitos.enties.Api;

public interface ApiRepo extends JpaRepository<Api, Integer> {

    @Query(
            "select case when count(a) > 0 then true else false end from Api a where a.id = ?1"
    )
    Boolean isApiExitsById(Integer id);

}
```

ApiService

```
package x.y.z.mokitos;

import org.springframework.stereotype.Service;
import x.y.z.mokitos.enties.Api;
import x.y.z.mokitos.repo.ApiRepo;

import java.util.List;

@Service
public class ApiService {

    private ApiRepo apiRepo;

    public ApiService(ApiRepo apiRepo) {
        this.apiRepo = apiRepo;
    }

    public List<Api> list() {
        return apiRepo.findAll();
    }
}
```

controller

```
package x.y.z.mokitos;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    private ApiService apiService;

    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    @GetMapping("apis")
    public ResponseEntity<?> apis() {
        return ResponseEntity.ok(this.apiService.list());
    }
}
```

集成测试

```
package x.y.z.mokitos.repo;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import x.y.z.mokitos.enties.Api;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ApiRepoTest {

    @Resource
    private ApiRepo apiRepo;

    @Test
    void isApiExitsById() {
        Api api = new Api(1, "GET", "/");
        apiRepo.save(api);
        Boolean apiExitsById = apiRepo.isApiExitsById(1);
        AssertionsForClassTypes.assertThat(apiExitsById).isTrue();
    }
}
```

mock测试

```
package x.y.z.mokitos;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import x.y.z.mokitos.enties.Api;
import x.y.z.mokitos.repo.ApiRepo;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ApiServiceTest {

    @Mock
    private ApiRepo apiRepo;

    private ApiService apiService;

    @BeforeEach
    void setup() {
        this.apiService = new ApiService(this.apiRepo);
    }

    // 验证行为是发生，包括执行顺序
    @Test
    void list() {

        Api api = new Api(2, "PUT", "/api");
        Mockito.when(apiRepo.findAll()).thenReturn(Collections.singletonList(api));

        apiService.list();
        Mockito.verify(apiRepo).findAll();
    }

    // 返回模拟数据
    @Test
    void when_thenReturn() {

        Api api = new Api(2, "PUT", "/api");
        Mockito.when(apiRepo.findAll()).thenReturn(Collections.singletonList(api));

        List<Api> list = apiService.list();
        AssertionsForClassTypes.assertThat(list.size()).isEqualTo(1);
        AssertionsForClassTypes.assertThat(list.get(0).getUri()).isEqualTo(api.getUri());
        AssertionsForClassTypes.assertThat(list.get(0).getId()).isEqualTo(api.getId());
        AssertionsForClassTypes.assertThat(list.get(0)).isEqualTo(api);
    }
}
```

单独测试controller

```
package x.y.z.mokitos;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import x.y.z.mokitos.enties.Api;
import x.y.z.mokitos.repo.ApiRepo;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 单独测试controller
 */
@WebMvcTest(ApiController.class)
class ApiControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private ApiRepo apiRepo;

    @MockBean
    private ApiService apiService;

    @Test
    void get_by_id() throws Exception {
        Api api = new Api(3, "POST", "/api/delete");
        given(apiRepo.getById(anyInt())).willReturn(api);
        this.mvc.perform(get("/api/3").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(api.getUri())));
    }
}
```

