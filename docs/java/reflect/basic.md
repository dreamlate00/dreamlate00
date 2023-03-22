## 常用方法

准备

```
static class Arg{
        String x1,x2,x3;

        public Arg() {
        }

        public Arg(String x1, String x2, String x3) {
            this.x1 = x1;
            this.x2 = x2;
            this.x3 = x3;
        }

        public String getX1() {
            return x1;
        }

        public void setX1(String x1) {
            this.x1 = x1;
        }

        public String getX2() {
            return x2;
        }

        public void setX2(String x2) {
            this.x2 = x2;
        }

        public String getX3() {
            return x3;
        }

        public void setX3(String x3) {
            this.x3 = x3;
        }
    }
```

0 name

```
@Test
    public void basic() {
        Class<X> clazz = X.class;
        String name = clazz.getName();
        String simpleName = clazz.getSimpleName();
        String packageName = clazz.getPackage().getName();
        System.out.printf("name :%s\n",name);
        System.out.printf("simpleName :%s\n",simpleName);
        System.out.printf("packageName :%s\n",packageName);
    }
```

1 获取所有方法

```

```

2  获取所有属性

```

```

3 超类

4 数组

5 泛型

6 注解