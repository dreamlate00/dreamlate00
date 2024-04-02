待测试的controller类

```
@RestController
@RequestMapping("form")
@Slf4j
public class FormTestController {

    @PostMapping("v1")
    @ResponseBody
    public Map<String,Object> form1(@RequestParam Map<String,Object> form){
        log.info(JSON.toJSONString(form));
        return Collections.singletonMap("source",form);
    }

    @PostMapping("v2")
    @ResponseBody
    public Map<String,Object> form2(Map<String,Object> form){
        log.info(JSON.toJSONString(form));
        return Collections.singletonMap("source",form);
    }

    @PostMapping("v3")
    @ResponseBody
    public Map<String,Object> form3(Form form){
        log.info(JSON.toJSONString(form));
        return Collections.singletonMap("source",form);
    }

    @PostMapping("v4")
    @ResponseBody
    public Map<String,Object> form4(@RequestParam Form form){
        log.info(JSON.toJSONString(form));
        return Collections.singletonMap("source",form);
    }
    @Data
    static class Form{
        String x1,x2;
    }
}
```

测试代码

```java
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebMvcTest
class FormTestControllerTest2 {

    @Autowired
    private MockMvc mvc;

    Form form ;

    @BeforeEach
    void setUp() {
        Map<String, String> map = Collections.singletonMap("name", "掌声");
        form = new Form();
        form.setX1("是下辖的");
        form.setX2(JSON.toJSONString(map));
    }

    @Test
    @DisplayName("Form1")
    void Form1() throws Exception {
        this.mvc.perform(post("/form/v1").param("x1",this.form.x1).param("x2",this.form.x2))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Data
    static class Form{
        String x1,x2;
    }
}
```

使用WebMvcTest对web相关的controller进行测试，可以指定要测试的conteoller的类名。通过MockMvc来发送请求。

这里最好配合Mockbean来使用，减少其他类的影响。