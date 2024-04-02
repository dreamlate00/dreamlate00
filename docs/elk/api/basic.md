```sh
curl -u elastic:<password> <elasticsearch_endpoint>/
```

查询带用户名密码



插入数据

单笔

```
POST logs-my_app-default/_doc
{
  "@timestamp": "2099-05-06T16:21:15.000Z",
  "event": {
    "original": "192.0.2.42 - - [06/May/2099:16:21:15 +0000] \"GET /images/bg.jpg HTTP/1.0\" 200 24736"
  }
}
```

多笔

```
PUT logs-my_app-default/_bulk
{ "create": { } }
{ "@timestamp": "2099-05-07T16:24:32.000Z", "event": { "original": "192.0.2.242 - - [07/May/2020:16:24:32 -0500] \"GET /images/hm_nbg.jpg HTTP/1.0\" 304 0" } }
{ "create": { } }
{ "@timestamp": "2099-05-08T16:25:42.000Z", "event": { "original": "192.0.2.255 - - [08/May/2099:16:25:42 +0000] \"GET /favicon.ico HTTP/1.0\" 200 3638" } }
```



搜索

指定排序方式

```
GET logs-my_app-default/_search
{
  "query": {
    "match_all": { }
  },
  "sort": [
    {
      "@timestamp": "desc"
    }
  ]
}
```

指定字段

```console
GET logs-my_app-default/_search
{
  "query": {
    "match_all": { }
  },
  "fields": [
    "@timestamp"
  ],
  "_source": false,
  "sort": [
    {
      "@timestamp": "desc"
    }
  ]
}
```

范围查询

```console
GET logs-my_app-default/_search
{
  "query": {
    "range": {
      "@timestamp": {
        "gte": "2099-05-05",
        "lt": "2099-05-08"
      }
    }
  },
  "fields": [
    "@timestamp"
  ],
  "_source": false,
  "sort": [
    {
      "@timestamp": "desc"
    }
  ]
}
```

使用运行时字段

```
GET logs-my_app-default/_search
{
  "runtime_mappings": {
    "source.ip": {
      "type": "ip",
      "script": """
        String sourceip=grok('%{IPORHOST:sourceip} .*').extract(doc[ "event.original" ].value)?.sourceip;
        if (sourceip != null) emit(sourceip);
      """
    }
  },
  "query": {
    "range": {
      "@timestamp": {
        "gte": "2099-05-05",
        "lt": "2099-05-08"
      }
    }
  },
  "fields": [
    "@timestamp",
    "source.ip"
  ],
  "_source": false,
  "sort": [
    {
      "@timestamp": "desc"
    }
  ]
}
```

多个搜索条件

```
GET logs-my_app-default/_search
{
  "runtime_mappings": {
    "source.ip": {
      "type": "ip",
      "script": """
        String sourceip=grok('%{IPORHOST:sourceip} .*').extract(doc[ "event.original" ].value)?.sourceip;
        if (sourceip != null) emit(sourceip);
      """
    }
  },
  "query": {
    "bool": {
      "filter": [
        {
          "range": {
            "@timestamp": {
              "gte": "2099-05-05",
              "lt": "2099-05-08"
            }
          }
        },
        {
          "range": {
            "source.ip": {
              "gte": "192.0.2.0",
              "lte": "192.0.2.240"
            }
          }
        }
      ]
    }
  },
  "fields": [
    "@timestamp",
    "source.ip"
  ],
  "_source": false,
  "sort": [
    {
      "@timestamp": "desc"
    }
  ]
}
```

聚合

```
GET logs-my_app-default/_search
{
  "runtime_mappings": {
    "http.response.body.bytes": {
      "type": "long",
      "script": """
        String bytes=grok('%{COMMONAPACHELOG}').extract(doc[ "event.original" ].value)?.bytes;
        if (bytes != null) emit(Integer.parseInt(bytes));
      """
    }
  },
  "aggs": {
    "average_response_size":{
      "avg": {
        "field": "http.response.body.bytes"
      }
    }
  },
  "query": {
    "bool": {
      "filter": [
        {
          "range": {
            "@timestamp": {
              "gte": "2099-05-05",
              "lt": "2099-05-08"
            }
          }
        }
      ]
    }
  },
  "fields": [
    "@timestamp",
    "http.response.body.bytes"
  ],
  "_source": false,
  "sort": [
    {
      "@timestamp": "desc"
    }
  ]
}
```

删除索引

```
DELETE _data_stream/logs-my_app-default
```

