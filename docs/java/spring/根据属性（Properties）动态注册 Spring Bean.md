依赖

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter</artifactId>
    <version>3.2.3</version>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <version>3.2.3</version>
    <scope>test</scope>
</dependency>
```

application.yml

```
api:
  clients:
    - name: example  
      url: https://api.example.com
      key: 12345
    - name: anotherexample
      url: https://api.anotherexample.com
      key: 67890
```

bean

```
public class ApiClient {
    private String name;
    private String url;
    private String key;
    // Getter、Setter 和构造函数
    
    public String getConnectionProperties() {
        return "Connecting to " + name + " at " + url;     
    }
}
```



自定义BeanDefinitionRegistryPostProcessor

```
public class ApiClientConfiguration implements BeanDefinitionRegistryPostProcessor {
    private static final String API_CLIENT_BEAN_NAME = "apiClient_";
    List<ApiClient> clients;

    public ApiClientConfiguration(Environment environment) {
        Binder binder = Binder.get(environment);
        List<HashMap> properties = binder.bind("api.clients", Bindable.listOf(HashMap.class)).get();
        clients = properties.stream().map(client -> new ApiClient(String.valueOf(client.get("name")),
                String.valueOf(client.get("url")), String.valueOf(client.get("key")))).toList();
    }    

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

        clients.forEach(client -> {
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(ApiClient.class);
            builder.addPropertyValue("name", client.getName());
            builder.addPropertyValue("url", client.getUrl());
            builder.addPropertyValue("key", client.getkey());
            registry.registerBeanDefinition(API_CLIENT_BEAN_NAME + client.getName(), builder.getBeanDefinition());
        });
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    }
}
```

