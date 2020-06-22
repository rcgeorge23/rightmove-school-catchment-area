#!/usr/bin/env bash

echo "Standing up greenmail"
docker run -d -t -i \
    --name lcag-mail \
    --network lcag-automation-network \
    -p 3025:3025 \
    -p 3110:3110 \
    -p 3143:3143 \
    -p 3465:3465 \
    -p 3993:3993 \
    -p 3995:3995 \
    greenmail/standalone:1.5.7

echo "Standing up mysql"
docker run -d \
    --name lcag-mysql \
    --network lcag-automation-network \
    -p 4306:3306 \
    -e MYSQL_ROOT_PASSWORD=p@ssword \
    -e MYSQL_DATABASE=mybb \
    -e MYSQL_USER=user \
    -e MYSQL_PASSWORD=p@ssword \
    mysql:5.6

echo "standing up sftp server"
docker run -d \
    --name lcag-sftp \
    --network lcag-automation-network \
    -p 2222:22 \
    atmoz/sftp \
    user:password:::upload