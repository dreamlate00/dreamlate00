请求时带上用户名密码

```
curl -v -u username:password -X POST 'host:port/_sql/translate' -H 'Content-Type:application/json' -d'
{
  "query": "SELECT title,singer FROM lrc  LIMIT 10"
}	
'
```

