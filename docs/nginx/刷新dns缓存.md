### 使用nginx的resolver

```
server {
    ....
    resolver 127.0.0.1 valid=60s;
    resolver_timeout 3s;

    set $proxy_url "proxy.test.com";
    location / {
        proxy_set_header Host proxy.test.com;
        proxy_pass http://$proxy_url;
    }
}

```

