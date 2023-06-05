/etc/sysconfig/network-scripts

ifcfg-ens33 原始备份

```
TYPE="Ethernet"
PROXY_METHOD="none"
BROWSER_ONLY="no"
BOOTPROTO="dhcp"
DEFROUTE="yes"
IPV4_FAILURE_FATAL="no"
IPV6INIT="yes"
IPV6_AUTOCONF="yes"
IPV6_DEFROUTE="yes"
IPV6_FAILURE_FATAL="no"
IPV6_ADDR_GEN_MODE="stable-privacy"
NAME="ens33"
UUID="9db26a20-4fc3-49e7-b331-c29e6be116c1"
DEVICE="ens33"
ONBOOT="yes"
IPV6_PRIVACY="no"
```

固定ip

```
TYPE="Ethernet"
PROXY_METHOD="none"
BROWSER_ONLY="no"
DEFROUTE="yes"
IPV4_FAILURE_FATAL="no"
IPV6INIT="yes"
IPV6_AUTOCONF="yes"
IPV6_DEFROUTE="yes"
IPV6_FAILURE_FATAL="no"
IPV6_ADDR_GEN_MODE="stable-privacy"
NAME="ens33"
UUID="9db26a20-4fc3-49e7-b331-c29e6be116c1"
DEVICE="ens33"
IPV6_PRIVACY="no"
# 这里开始
BOOTPROTO="none"
ONBOOT="yes"
NM_CONTROLLED="yes"
IPADDR="192.168.234.128"  #这里就是你设置的固定IP
NETMASK="255.255.255.0"
GATEWAY="192.168.234.2"
```

重启网卡

```shell
service network restart
```

