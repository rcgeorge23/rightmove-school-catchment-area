#!/usr/bin/env bash

echo "Standing up right move search application"
docker run -d \
    -e "SPRING_PROFILES_ACTIVE=prod" \
	  -e "VIRTUAL_PORT=9000" \
	  -e "SERVER_PORT=9000" \
	  --name rightmove-search \
    -p 9000:9000 -p 5005:5005 \
    -t dockernovinet/rightmove-search

echo "Waiting for application status url to respond with 200"
while [[ "$(curl -s -o /dev/null -w ''%{http_code}'' localhost:9000/status)" != "200" ]]; do sleep 5; done