原理

```java
@Test
    void test_simplest(){
        Socket socket = new Socket();
        try {
            // 建立连接
            socket.setReuseAddress(true);
            socket.setKeepAlive(true);
            socket.setTcpNoDelay(true);
            socket.setSoLinger(true, 0);
            socket.connect(new InetSocketAddress("192.168.234.130", 6379), 2000);
            socket.setSoTimeout(2000);

            RedisInputStream inputStream = new RedisInputStream(socket.getInputStream());
            RedisOutputStream outputStream = new RedisOutputStream(socket.getOutputStream());
			
            // 发送报文
            final byte DOLLAR_BYTE = '$';
            final byte ASTERISK_BYTE = '*';

            final byte[] command = "PING".getBytes("UTF-8");
            final byte[][] EMPTY_ARGS = new byte[0][];

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            try {
                outputStream.write(ASTERISK_BYTE);
                outputStream.writeIntCrLf(EMPTY_ARGS.length + 1);
                outputStream.write(DOLLAR_BYTE);
                outputStream.writeIntCrLf(command.length);
                outputStream.write(command);
                outputStream.writeCrLf();

                for (final byte[] arg : EMPTY_ARGS) {
                    outputStream.write(DOLLAR_BYTE);
                    outputStream.writeIntCrLf(arg.length);
                    outputStream.write(arg);
                    outputStream.writeCrLf();
                }
            } catch (IOException e) {
                throw new JedisConnectionException(e);
            }

            outputStream.flush();

//            final byte[] resp = (byte[])Protocol.read(inputStream);
            // 解析报文
            inputStream.readByte();
            final byte[] resp = inputStream.readLineBytes();
            String encode = new String(resp, "UTF-8");
            System.out.println(encode);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
```

