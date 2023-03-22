# Mapping

> Mapping is the process of defining how a document, and the fields it contains, are stored and indexed.
>
> Each document is a collection of fields, which each have their own [data type](https://www.elastic.co/guide/en/elasticsearch/reference/7.13/mapping-types.html). When mapping your data, you create a mapping definition, which contains a list of fields that are pertinent to the document. A mapping definition also includes [metadata fields](https://www.elastic.co/guide/en/elasticsearch/reference/7.13/mapping-fields.html), like the `_source` field, which customize how a document’s associated metadata is handled.
>
> Use *dynamic mapping* and *explicit mapping* to define your data. Each method provides different benefits based on where you are in your data journey. For example, explicitly map fields where you don’t want to use the defaults, or to gain greater control over which fields are created. You can then allow Elasticsearch to add other fields dynamically



Dynamic mapping

动态date字段的映射，默认：yyyy/MM/dd HH:mm:ss Z||yyyy/MM/dd Z

```
PUT my-index-000001
{
  "mappings": {
    "dynamic_date_formats": ["MM/dd/yyyy"]
  }
}

PUT my-index-000001/_doc/1
{
  "create_date": "09/25/2015"
}
```

数字字段侦测

```
PUT my-index-000001
{
  "mappings": {
    "numeric_detection": true
  }
}

PUT my-index-000001/_doc/1
{
  "my_float":   "1.0", 
  "my_integer": "1" 
}
```

Explicit mapping

```
PUT /my-index-000001
{
  "mappings": {
    "properties": {
      "age":    { "type": "integer" },  
      "email":  { "type": "keyword"  }, 
      "name":   { "type": "text"  }     
    }
  }
}
```

更新字段

```
PUT /my-index-000001/_mapping
{
  "properties": {
    "employee-id": {
      "type": "keyword",
      "index": false
    }
  }
}
```

runtime fields