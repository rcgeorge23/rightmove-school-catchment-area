#!/usr/bin/env bash

echo "Standing up lcag automation application"
docker run -d \
    -e "SPRING_PROFILES_ACTIVE=prod" \
	-e "VIRTUAL_PORT=9000" \
	-e "SERVER_PORT=9000" \
	--name lcag-application \
    --network lcag-automation-network \
    -p 9000:9000 -p 5005:5005 \
    -t dockernovinet/lcag-automation

echo "Waiting for application status url to respond with 200"
while [[ "$(curl -s -o /dev/null -w ''%{http_code}'' localhost:8282/status)" != "200" ]]; do sleep 5; done