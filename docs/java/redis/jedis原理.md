实际集成自BinaryJedis，实现了JedisCommands, MultiKeyCommands,AdvancedJedisCommands, ScriptingCommands, BasicCommands, ClusterCommands, SentinelCommands接口，实际实现是在BinaryJedis

```java
public class Jedis extends BinaryJedis implements JedisCommands, MultiKeyCommands,
    AdvancedJedisCommands, ScriptingCommands, BasicCommands, ClusterCommands, SentinelCommands {

  protected Pool<Jedis> dataSource = null;

}

```

BinaryJedis实现了各种redis操作的接口，实际发送请求的的是Client

```java
public class BinaryJedis implements BasicCommands, BinaryJedisCommands, MultiKeyBinaryCommands,
        AdvancedBinaryJedisCommands, BinaryScriptingCommands, Closeable {
    protected Client client = null;
    protected Transaction transaction = null;
    protected Pipeline pipeline = null;

    public BinaryJedis() {
        client = new Client();
    }
	...
}
```



```java
public class Client extends BinaryClient implements Commands {
	...
}

public class BinaryClient extends Connection {
	....
}

public class Connection implements Closeable {
	...
      private String host = Protocol.DEFAULT_HOST;
      private int port = Protocol.DEFAULT_PORT;
      private Socket socket;
      private RedisOutputStream outputStream;
      private RedisInputStream inputStream;
    
 protected Connection sendCommand(final Command cmd, final byte[]... args) {
    try {
      connect();
      Protocol.sendCommand(outputStream, cmd, args);
      pipelinedCommands++;
      return this;
    } catch (JedisConnectionException ex) {
      /*
       * When client send request which formed by invalid protocol, Redis send back error message
       * before close connection. We try to read it to provide reason of failure.
       */
      try {
        String errorMessage = Protocol.readErrorLineIfPossible(inputStream);
        if (errorMessage != null && errorMessage.length() > 0) {
          ex = new JedisConnectionException(errorMessage, ex.getCause());
        }
      } catch (Exception e) {
        /*
         * Catch any IOException or JedisConnectionException occurred from InputStream#read and just
         * ignore. This approach is safe because reading error message is optional and connection
         * will eventually be closed.
         */
      }
      // Any other exceptions related to connection?
      broken = true;
      throw ex;
    }
  }
  public boolean isConnected() {
    return socket != null && socket.isBound() && !socket.isClosed() && socket.isConnected()
        && !socket.isInputShutdown() && !socket.isOutputShutdown();
  }  
  public void connect() {
    if (!isConnected()) {
      try {
        socket = new Socket();
        // ->@wjw_add
        socket.setReuseAddress(true);
        socket.setKeepAlive(true); // Will monitor the TCP connection is valid
        socket.setTcpNoDelay(true); // Socket buffer Whetherclosed, to ensure timely delivery of data
        socket.setSoLinger(true, 0); // Control calls close () method, the underlying socket is closed immediately
        // <-@wjw_add

        socket.connect(new InetSocketAddress(host, port), connectionTimeout);
        socket.setSoTimeout(soTimeout);

        if (ssl) {
          if (null == sslSocketFactory) {
            sslSocketFactory = (SSLSocketFactory)SSLSocketFactory.getDefault();
          }
          socket = (SSLSocket) sslSocketFactory.createSocket(socket, host, port, true);
          if (null != sslParameters) {
            ((SSLSocket) socket).setSSLParameters(sslParameters);
          }
          if ((null != hostnameVerifier) &&
              (!hostnameVerifier.verify(host, ((SSLSocket) socket).getSession()))) {
            String message = String.format(
                "The connection to '%s' failed ssl/tls hostname verification.", host);
            throw new JedisConnectionException(message);
          }
        }

        outputStream = new RedisOutputStream(socket.getOutputStream());
        inputStream = new RedisInputStream(socket.getInputStream());
      } catch (IOException ex) {
        broken = true;
        throw new JedisConnectionException("Failed connecting to host " 
            + host + ":" + port, ex);
      }
    }
  }

  @Override
  public void close() {
    disconnect();
  }

  public void disconnect() {
    if (isConnected()) {
      try {
        outputStream.flush();
        socket.close();
      } catch (IOException ex) {
        broken = true;
        throw new JedisConnectionException(ex);
      } finally {
        IOUtils.closeQuietly(socket);
      }
    }
  }
}
```



```java
package redis.clients.jedis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import redis.clients.jedis.exceptions.JedisAskDataException;
import redis.clients.jedis.exceptions.JedisBusyException;
import redis.clients.jedis.exceptions.JedisClusterException;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.exceptions.JedisMovedDataException;
import redis.clients.jedis.exceptions.JedisNoScriptException;
import redis.clients.util.RedisInputStream;
import redis.clients.util.RedisOutputStream;
import redis.clients.util.SafeEncoder;

public final class Protocol {

  private static final String ASK_RESPONSE = "ASK";
  private static final String MOVED_RESPONSE = "MOVED";
  private static final String CLUSTERDOWN_RESPONSE = "CLUSTERDOWN";
  private static final String BUSY_RESPONSE = "BUSY";
  private static final String NOSCRIPT_RESPONSE = "NOSCRIPT";

  public static final String DEFAULT_HOST = "localhost";
  public static final int DEFAULT_PORT = 6379;
  public static final int DEFAULT_SENTINEL_PORT = 26379;
  public static final int DEFAULT_TIMEOUT = 2000;
  public static final int DEFAULT_DATABASE = 0;

  public static final String CHARSET = "UTF-8";

  public static final byte DOLLAR_BYTE = '$';
  public static final byte ASTERISK_BYTE = '*';
  public static final byte PLUS_BYTE = '+';
  public static final byte MINUS_BYTE = '-';
  public static final byte COLON_BYTE = ':';

  public static final String SENTINEL_MASTERS = "masters";
  public static final String SENTINEL_GET_MASTER_ADDR_BY_NAME = "get-master-addr-by-name";
  public static final String SENTINEL_RESET = "reset";
  public static final String SENTINEL_SLAVES = "slaves";
  public static final String SENTINEL_FAILOVER = "failover";
  public static final String SENTINEL_MONITOR = "monitor";
  public static final String SENTINEL_REMOVE = "remove";
  public static final String SENTINEL_SET = "set";

  public static final String CLUSTER_NODES = "nodes";
  public static final String CLUSTER_MEET = "meet";
  public static final String CLUSTER_RESET = "reset";
  public static final String CLUSTER_ADDSLOTS = "addslots";
  public static final String CLUSTER_DELSLOTS = "delslots";
  public static final String CLUSTER_INFO = "info";
  public static final String CLUSTER_GETKEYSINSLOT = "getkeysinslot";
  public static final String CLUSTER_SETSLOT = "setslot";
  public static final String CLUSTER_SETSLOT_NODE = "node";
  public static final String CLUSTER_SETSLOT_MIGRATING = "migrating";
  public static final String CLUSTER_SETSLOT_IMPORTING = "importing";
  public static final String CLUSTER_SETSLOT_STABLE = "stable";
  public static final String CLUSTER_FORGET = "forget";
  public static final String CLUSTER_FLUSHSLOT = "flushslots";
  public static final String CLUSTER_KEYSLOT = "keyslot";
  public static final String CLUSTER_COUNTKEYINSLOT = "countkeysinslot";
  public static final String CLUSTER_SAVECONFIG = "saveconfig";
  public static final String CLUSTER_REPLICATE = "replicate";
  public static final String CLUSTER_SLAVES = "slaves";
  public static final String CLUSTER_FAILOVER = "failover";
  public static final String CLUSTER_SLOTS = "slots";
  public static final String PUBSUB_CHANNELS = "channels";
  public static final String PUBSUB_NUMSUB = "numsub";
  public static final String PUBSUB_NUM_PAT = "numpat";

  public static final byte[] BYTES_TRUE = toByteArray(1);
  public static final byte[] BYTES_FALSE = toByteArray(0);

  public static final byte[] POSITIVE_INFINITY_BYTES = "+inf".getBytes();
  public static final byte[] NEGATIVE_INFINITY_BYTES = "-inf".getBytes();

  private Protocol() {
    // this prevent the class from instantiation
  }

  public static void sendCommand(final RedisOutputStream os, final Command command,
      final byte[]... args) {
    sendCommand(os, command.raw, args);
  }

  private static void sendCommand(final RedisOutputStream os, final byte[] command,
      final byte[]... args) {
    try {
      os.write(ASTERISK_BYTE);
      os.writeIntCrLf(args.length + 1);
      os.write(DOLLAR_BYTE);
      os.writeIntCrLf(command.length);
      os.write(command);
      os.writeCrLf();

      for (final byte[] arg : args) {
        os.write(DOLLAR_BYTE);
        os.writeIntCrLf(arg.length);
        os.write(arg);
        os.writeCrLf();
      }
    } catch (IOException e) {
      throw new JedisConnectionException(e);
    }
  }

  private static void processError(final RedisInputStream is) {
    String message = is.readLine();
    // TODO: I'm not sure if this is the best way to do this.
    // Maybe Read only first 5 bytes instead?
    if (message.startsWith(MOVED_RESPONSE)) {
      String[] movedInfo = parseTargetHostAndSlot(message);
      throw new JedisMovedDataException(message, new HostAndPort(movedInfo[1],
          Integer.parseInt(movedInfo[2])), Integer.parseInt(movedInfo[0]));
    } else if (message.startsWith(ASK_RESPONSE)) {
      String[] askInfo = parseTargetHostAndSlot(message);
      throw new JedisAskDataException(message, new HostAndPort(askInfo[1],
          Integer.parseInt(askInfo[2])), Integer.parseInt(askInfo[0]));
    } else if (message.startsWith(CLUSTERDOWN_RESPONSE)) {
      throw new JedisClusterException(message);
    } else if (message.startsWith(BUSY_RESPONSE)) {
      throw new JedisBusyException(message);
    } else if (message.startsWith(NOSCRIPT_RESPONSE) ) {
      throw new JedisNoScriptException(message);
    }
    throw new JedisDataException(message);
  }

  public static String readErrorLineIfPossible(RedisInputStream is) {
    final byte b = is.readByte();
    // if buffer contains other type of response, just ignore.
    if (b != MINUS_BYTE) {
      return null;
    }
    return is.readLine();
  }

  private static String[] parseTargetHostAndSlot(String clusterRedirectResponse) {
    String[] response = new String[3];
    String[] messageInfo = clusterRedirectResponse.split(" ");
    String[] targetHostAndPort = HostAndPort.extractParts(messageInfo[2]);
    response[0] = messageInfo[1];
    response[1] = targetHostAndPort[0];
    response[2] = targetHostAndPort[1];
    return response;
  }

  private static Object process(final RedisInputStream is) {
    final byte b = is.readByte();
    switch(b) {
    case PLUS_BYTE:
      return processStatusCodeReply(is);
    case DOLLAR_BYTE:
      return processBulkReply(is);
    case ASTERISK_BYTE:
      return processMultiBulkReply(is);
    case COLON_BYTE:
      return processInteger(is);
    case MINUS_BYTE:
      processError(is);
      return null;
    default:
      throw new JedisConnectionException("Unknown reply: " + (char) b);
    }
  }

  private static byte[] processStatusCodeReply(final RedisInputStream is) {
    return is.readLineBytes();
  }

  private static byte[] processBulkReply(final RedisInputStream is) {
    final int len = is.readIntCrLf();
    if (len == -1) {
      return null;
    }

    final byte[] read = new byte[len];
    int offset = 0;
    while (offset < len) {
      final int size = is.read(read, offset, (len - offset));
      if (size == -1) throw new JedisConnectionException(
          "It seems like server has closed the connection.");
      offset += size;
    }

    // read 2 more bytes for the command delimiter
    is.readByte();
    is.readByte();

    return read;
  }

  private static Long processInteger(final RedisInputStream is) {
    return is.readLongCrLf();
  }

  private static List<Object> processMultiBulkReply(final RedisInputStream is) {
    final int num = is.readIntCrLf();
    if (num == -1) {
      return null;
    }
    final List<Object> ret = new ArrayList<Object>(num);
    for (int i = 0; i < num; i++) {
      try {
        ret.add(process(is));
      } catch (JedisDataException e) {
        ret.add(e);
      }
    }
    return ret;
  }

  public static Object read(final RedisInputStream is) {
    return process(is);
  }

  public static final byte[] toByteArray(final boolean value) {
    return value ? BYTES_TRUE : BYTES_FALSE;
  }

  public static final byte[] toByteArray(final int value) {
    return SafeEncoder.encode(String.valueOf(value));
  }

  public static final byte[] toByteArray(final long value) {
    return SafeEncoder.encode(String.valueOf(value));
  }

  public static final byte[] toByteArray(final double value) {
    if (value == Double.POSITIVE_INFINITY) {
      return POSITIVE_INFINITY_BYTES;
    } else if (value == Double.NEGATIVE_INFINITY) {
      return NEGATIVE_INFINITY_BYTES;
    } else {
      return SafeEncoder.encode(String.valueOf(value));
    }
  }

  public static enum Command {
    PING, SET, GET, QUIT, EXISTS, DEL, TYPE, FLUSHDB, KEYS, RANDOMKEY, RENAME, RENAMENX, RENAMEX, DBSIZE, EXPIRE, EXPIREAT, TTL, SELECT, MOVE, FLUSHALL, GETSET, MGET, SETNX, SETEX, MSET, MSETNX, DECRBY, DECR, INCRBY, INCR, APPEND, SUBSTR, HSET, HGET, HSETNX, HMSET, HMGET, HINCRBY, HEXISTS, HDEL, HLEN, HKEYS, HVALS, HGETALL, RPUSH, LPUSH, LLEN, LRANGE, LTRIM, LINDEX, LSET, LREM, LPOP, RPOP, RPOPLPUSH, SADD, SMEMBERS, SREM, SPOP, SMOVE, SCARD, SISMEMBER, SINTER, SINTERSTORE, SUNION, SUNIONSTORE, SDIFF, SDIFFSTORE, SRANDMEMBER, ZADD, ZRANGE, ZREM, ZINCRBY, ZRANK, ZREVRANK, ZREVRANGE, ZCARD, ZSCORE, MULTI, DISCARD, EXEC, WATCH, UNWATCH, SORT, BLPOP, BRPOP, AUTH, SUBSCRIBE, PUBLISH, UNSUBSCRIBE, PSUBSCRIBE, PUNSUBSCRIBE, PUBSUB, ZCOUNT, ZRANGEBYSCORE, ZREVRANGEBYSCORE, ZREMRANGEBYRANK, ZREMRANGEBYSCORE, ZUNIONSTORE, ZINTERSTORE, ZLEXCOUNT, ZRANGEBYLEX, ZREVRANGEBYLEX, ZREMRANGEBYLEX, SAVE, BGSAVE, BGREWRITEAOF, LASTSAVE, SHUTDOWN, INFO, MONITOR, SLAVEOF, CONFIG, STRLEN, SYNC, LPUSHX, PERSIST, RPUSHX, ECHO, LINSERT, DEBUG, BRPOPLPUSH, SETBIT, GETBIT, BITPOS, SETRANGE, GETRANGE, EVAL, EVALSHA, SCRIPT, SLOWLOG, OBJECT, BITCOUNT, BITOP, SENTINEL, DUMP, RESTORE, PEXPIRE, PEXPIREAT, PTTL, INCRBYFLOAT, PSETEX, CLIENT, TIME, MIGRATE, HINCRBYFLOAT, SCAN, HSCAN, SSCAN, ZSCAN, WAIT, CLUSTER, ASKING, PFADD, PFCOUNT, PFMERGE, READONLY, GEOADD, GEODIST, GEOHASH, GEOPOS, GEORADIUS, GEORADIUSBYMEMBER, BITFIELD;

    public final byte[] raw;

    Command() {
      raw = SafeEncoder.encode(this.name());
    }

  }

  public static enum Keyword {
    AGGREGATE, ALPHA, ASC, BY, DESC, GET, LIMIT, MESSAGE, NO, NOSORT, PMESSAGE, PSUBSCRIBE, PUNSUBSCRIBE, OK, ONE, QUEUED, SET, STORE, SUBSCRIBE, UNSUBSCRIBE, WEIGHTS, WITHSCORES, RESETSTAT, RESET, FLUSH, EXISTS, LOAD, KILL, LEN, REFCOUNT, ENCODING, IDLETIME, AND, OR, XOR, NOT, GETNAME, SETNAME, LIST, MATCH, COUNT, PING, PONG;
    public final byte[] raw;

    Keyword() {
      raw = SafeEncoder.encode(this.name().toLowerCase(Locale.ENGLISH));
    }
  }
}

```

