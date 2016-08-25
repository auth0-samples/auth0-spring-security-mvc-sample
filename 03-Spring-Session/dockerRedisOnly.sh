#!/bin/bash
docker pull redis
docker run -p 6379:6379 --name redis-server -d redis

