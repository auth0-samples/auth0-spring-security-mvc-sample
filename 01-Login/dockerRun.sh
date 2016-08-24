#!/bin/bash
docker build -t auth0/auth0-spring-security-mvc-sample -f Dockerfile .
docker run -it -p 3099:3099 \
       -v $(pwd):/usr/src/app \
       --name auth0-spring-security-mvc-sample auth0/auth0-spring-security-mvc-sample
