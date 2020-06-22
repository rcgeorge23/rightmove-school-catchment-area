#!/usr/bin/env bash

./standup_supporting_containers.sh

./standup_application.sh

echo "Waiting for application status url to respond with 200"
while [[ "$(curl -s -o /dev/null -w ''%{http_code}'' localhost:8282/status)" != "200" ]]; do sleep 5; done