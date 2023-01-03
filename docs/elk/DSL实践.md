###### DEMO1

> 指定需要返回的字段

```shll
POST access/_search
{
	"_source":["request","response","trace_id","time_cost"]
	"query":{
		"match_all":{}
	}
}
```

###### DEMO2

> 指定显示字段和排除显示的字段

```
POST access/_search
{
	"_source":{
		"include":["url","method","request","response","trace_id","time_cost"],
		"exclude":[“host”],
	}
	"query":{
		"match_all":{}
	}
}
```



###### 创建索引

```
PUT gumda_seed
{
  "aliases": {
    "gumda": {}
  }, 
  "settings": {
    "number_of_shards": 1,
    "number_of_replicas": 0
  },
  "mappings": {
    "properties": {
      "model":{"type": "keyword"},
      "name":{"type": "keyword"}
    }
    
  }
}
```

测试数据

```
POST gumda_seed/_bulk
{ "index" : { "_id" : "GAT-X102" } }
{ "model" : "GAT-X102","name":"决斗高达" }
{ "index" : { "_id" : "GAT-X103" } }
{ "model" : "GAT-X103","name":"暴风高达" }
{ "index" : { "_id" : "GAT-X105" } }
{ "model" : "GAT-X105","name":"强袭高达" }
{ "index" : { "_id" : "GAT-X252" } }
{ "model" : "GAT-X252","name":"迅雷高达" }
{ "index" : { "_id" : "GAT-X303" } }
{ "model" : "GAT-X303","name":"圣盾高达" }
```

