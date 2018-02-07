#!/usr/bin/env bash
docker build -t auth0-spring-security-mvc-01-login .
docker run -p 3000:3000 -v ~/.m2:/home/gradle/.gradle -it auth0-spring-security-mvc-01-login
