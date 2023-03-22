创建声生命周期策略

- 删除不重要，主要是每天一个索引
- 这个可以在kbn配

```
PUT _ilm/policy/timeseries_policy
{
  "policy": {
    "phases": {
      "hot": {                                
        "actions": {
          "rollover": {
            //rollover前距离索引的创建时间最大为7天
            "max_age": "7d",
			//rollover前索引的最大大小不超过50G
            "max_size": "50G",
			//rollover前索引的最大文档数不超过1个（测试用）
            "max_docs": 1,
          }
        }
      },
      "delete": {
       "min_age": "365s",                     
        "actions": {
          "delete": {}                        
        }
      }
    }
  }
}
```

创建索引模板

```
PUT _index_template/timeseries_template
{
  "index_patterns": ["timeseries"],                   
  "data_stream": { },
  "template": {
    "settings": {
      "number_of_shards": 1,
      "number_of_replicas": 1,
      "index.lifecycle.name": "timeseries_policy"     
    }
  }
}
```

写入

```
PUT _data_stream/timeseries


POST timeseries/_doc
{
  "message": "logged the request",
  "@timestamp": "1633677855467"
}

PUT timeseries/_bulk
{ "create":{ } }
{"message": "logged the request1","@timestamp": "1633677862467"}
{ "create":{ } }
{"message": "logged the request2","@timestamp": "1633677872468"}
{ "create":{ } }
{"message": "logged the request3","@timestamp": "1633682619628"}


```

```
GET _data_stream/my-data-stream
DELETE _data_stream/my-data-stream

```

```
GET /my-data-stream/_search
{
  "seq_no_primary_term": true,
  "query": {
    "match": {
      "user.id": "yWIumJd7"
    }
  }
}

```

