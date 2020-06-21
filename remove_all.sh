#!/usr/bin/env bash

echo "Removing all running containers"
docker rm -f $(docker ps -a -q)