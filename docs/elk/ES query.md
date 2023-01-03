单个自动查询

1 term

```
GET /person/_search
{
	"query": {
		"term": {
			"name": {
				"value": "周公瑾"
			}
		}
	}
}
```

2 terms

```
GET /person/_search
{
	"query": {
		"terms": {
			"name": ["周公瑾","周瑜"]
		}
	}
}
```

3 range

```
GET /person/_search
{
	"query": {
		"range": {
			"age": {
				"from": 10,
				"to": 20,
				"include_lower": true,
				"include_upper": true,
				"boost": 1.0
			}
		}
	}
}
```

4 prefix

```
{
	"query": {
		"prefix": {
			"name": {
				"value": "曹"
			}
		}
	}
}
```

5 通配符wildcard

```
{
	"query": {
		"wildcard": {
			"name": {
				"value": "*德"
			}
		}
	}
}
```

复合查询

1 bool

```
{
   "bool" : {
      "must" :     [],
      "should" :   [],
      "must_not" : [],
   }
}

{
	"query": {
		"bool": {
			"must": [
				{
				    "term": {
						"sex": {
							"value": "女",
							"boost": 1.0
						}
					}
				},
				{
					"prefix": {
						"name": {
							"value": "张"
						}
					}
				}
			],
			"adjust_pure_negative": true,
			"boost": 1.0
		}
	}
}
```

2 filter

```
{
   "bool" : {
      "filter" :     [
      
      ]
   }
}

{
	"query": {
		"bool": {
			"filter": [
				{
					"term": {
						"sex": {
							"value": "男",
							"boost": 1.0
						}
					}
				}
			],
			"adjust_pure_negative": true,
			"boost": 1.0
		}
	}
}
```

聚合查询

1 max min avg

```
GET /person/_search
{
	"aggregations": {
		"max_age": {
			"max": {
				"field": "age"
			}
		}
	}
}
```

2 去重 cardinality

```
{
	"aggregations": {
		"sect_count": {
			"cardinality": {
				"field": "sect.keyword"
			}
		}
	}
}
```

3 分组统计

```
{
	"size": 0,
	"aggregations": {
		"sect_count": {
			"terms": {
				"field": "sect.keyword",
				"size": 10,
				"min_doc_count": 1,
				"shard_min_doc_count": 0,
				"show_term_doc_count_error": false,
				"order": [
					{
						"_count": "desc"
					},
					{
						"_key": "asc"
					}
				]
			}
		}
	}
}
```

4 多条件分组

```
{
	"aggregations": {
		"sect_count": {
			"terms": {
				"field": "sect.keyword",
				"size": 10
			},
			"aggregations": {
				"sex_count": {
					"terms": {
						"field": "sex.keyword",
						"size": 10
					}
				}
			}
		}
	}
}
```

5 过滤 + 聚合

```
GET /person/_search
{
	"query": {
		"term": {
			"sect.keyword": {
				"value": "明教",
				"boost": 1.0
			}
		}
	},
	"aggregations": {
		"max_age": {
			"max": {
				"field": "age"
			}
		}
	}
}
```

