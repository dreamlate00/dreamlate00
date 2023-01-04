依赖

```
<dependency>
    <groupId>io.lettuce</groupId>
    <artifactId>lettuce-core</artifactId>
    <version>5.1.8.RELEASE</version>
</dependency>
```

备注： redis版本》=2.6



基本使用

```java
	@Test
    public void connection_test() {
        RedisURI uri = RedisURI.create("redis://192.168.234.130:6379/");
        RedisClient client = RedisClient.create(uri);
        StatefulRedisConnection<String, String> connection = client.connect();
        RedisCommands<String, String> commands = connection.sync();
        String result = commands.ping();
        System.out.println(result);
        connection.close();
        client.shutdown();
    }
```

