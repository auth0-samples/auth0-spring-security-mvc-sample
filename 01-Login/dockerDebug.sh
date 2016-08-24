#!/bin/bash
docker build -t auth0/auth0-spring-security-mvc-sample -f Dockerfile .
docker build -t auth0/auth0-spring-security-mvc-sample-debug -f Dockerfile-debug .
docker run -it -p 3099:3099 -p 8000:8000 \
       -v $(pwd):/usr/src/app \
       --name auth0-spring-security-mvc-sample-debug auth0/auth0-spring-security-mvc-sample-debug

