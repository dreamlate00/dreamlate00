Examples



```shell
> SET user:1 salvatore
OK
> GET user:1
"salvatore"
```



```
SET ticket:27 "\"{'username': 'priya', 'ticket_id': 321}\"" EX 100
```



```
> INCR views:page:2
(integer) 1
> INCRBY views:page:2 10
(integer) 11
```





> By default, a single Redis string can be a maximum of 512 MB.



### Getting and setting Strings

- [`SET`](https://redis.io/commands/set) stores a string value.
- [`SETNX`](https://redis.io/commands/setnx) stores a string value only if the key doesn't already exist. Useful for implementing locks.
- [`GET`](https://redis.io/commands/get) retrieves a string value.
- [`MGET`](https://redis.io/commands/mget) retrieves multiple string values in a single operation.



## Performance

Most string operations are O(1), which means they're highly efficient. However, be careful with the [`SUBSTR`](https://redis.io/commands/substr), [`GETRANGE`](https://redis.io/commands/getrange), and [`SETRANGE`](https://redis.io/commands/setrange) commands, which can be O(n). These random-access string commands may cause performance issues when dealing with large strings.





## Alternatives

If you're storing structured data as a serialized string, you may also want to consider [Redis hashes](https://redis.io/docs/data-types/hashes) or [RedisJSON](https://redis.io/docs/stack/json).