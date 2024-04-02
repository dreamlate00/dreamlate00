nginx.conf

```
location /kba {
   proxy_pass http://127.0.0.1:5601;
}
```



kibana.yml

```
server.host: "127.0.0.1"
server.rewriteBasePath: true
server.rewriteBasePath: true
```

