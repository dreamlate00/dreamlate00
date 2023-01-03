测试写入到outputstream

```java
@Test
    void test_buffer() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            String s = "hello world";
            String charsetName = "UTF-8";
            outputStream.write(s.getBytes(charsetName));
            String string = outputStream.toString(charsetName);
            Assertions.assertEquals(s,string);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
```

