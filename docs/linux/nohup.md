```
cat > start.sh << "EOF"
#!/usr/bin/env bash
echo
echo "Starting sim-web..."
HOME=$(dirname $(dirname $0))
APP_NAME=sw
VERSION=1.0.0
nohup java -jar -Xms256m -Xmx256m ${HOME}/${APP_NAME}-${VERSION}.jar > log.log 2>&1 &
echo
EOF
```

