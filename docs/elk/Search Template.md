## ES模板搜索——Search Template

日常开发中我们可能需要频繁的使用一些类似的查询。为了减少重复的工作，我们可以将这些类似的查询写成模板，在后续只需要传递不同的参数来调用模板获取结果。

创建模板

```
GET _search/template
{
  "source" : {
    "query": { "match" : { "{{my_field}}" : "{{my_value}}" } },
    "size" : "{{my_size}}"
  },
  "params" : {
    "my_field" : "message",
    "my_value" : "foo",
    "my_size" : 5
  }
}
```

查看模板

```
GET _scripts/<templateid>
```

删除模板

```
DELETE _scripts/<templateid>
```

验证查询模板

```
GET _render/template
{
  "source": "{ \"query\": { \"terms\": {{#toJson}}statuses{{/toJson}} }}",
  "params": {
    "statuses" : {
        "status": [ "pending", "published" ]
    }
  }
}

GET _render/template/<template_name>
{
  "params": {
    "..."
  }
}
```

在查询中使用模板

```
GET _search/template
{
  "id": "<templateid>", 
  "params": {
    "query_string": "search for these words"
  }
}
```

