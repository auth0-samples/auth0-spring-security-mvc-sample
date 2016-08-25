# Spring Session example

This is a simple Java Spring MVC web application that is meant to be used as the starting point for [Auth0 Java Spring MVC Quickstart](https://auth0.com/docs/quickstart/webapp/java-spring-mvc). 

However, this sample builds on the Login Example by illustrating how to easily integrate with [Spring Session](http://projects.spring.io/spring-session/)

Since this sample is a Spring Boot application, it follows the []Spring Session for Spring Boot instructions](http://docs.spring.io/spring-session/docs/current/reference/html5/guides/boot.html#boot-sample)

In order to run this example you will need to have Java 7+, Maven, and Redis Server installed.

Check that your redis server is installed correctly:

```sh
redis-cli
```

If you are interested in running Redis in a Docker container instead of installing locally (and have Docker installed) then
simply run `dockerRedisOnly.sh`. If you are not using Docker, no problems just install locally instead.

Check that your maven version is 3.0.x or above:

```sh
mvn -v
```

In order to build and run the project you must execute:

```sh
mvn spring-boot:run
```

Then, go to [http://localhost:3099/login](http://localhost:3099/login).

Shut it down manually with Ctrl-C.

Documentation: [Login](https://auth0.com/docs/quickstart/webapp/java-spring-mvc/01-login)


### How does Spring-Session work:

We just have to add the following maven (pom.xml) dependencies;

```
<dependency>
    <groupId>org.springframework.session</groupId>
    <artifactId>spring-session</artifactId>
    <version>1.2.1.RELEASE</version>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-redis</artifactId>
</dependency>
```

And we add an extra configuration class:

```
package com.auth0.example;

import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

// tag::class[]
@EnableRedisHttpSession
public class HttpSessionConfig {
}
```

We add the following to the Overridden AppConfig (This is *IMPORTANT* to get Spring Session to work correctly with Spring Security and Spring Boot)

```
@Bean
public OrderedRequestContextFilter requestContextFilter() {
    return new OrderedRequestContextFilter();
}
```


Finally, we need to update our `src/main/resources/application.properties` with the redis specific properties to connect:

```
spring.redis.host=localhost
# spring.redis.password=
spring.redis.port=6379
```

All done!

### Inspecting Redis

Now, session state will be stored in `Redis`. 

You can see this using `redis-cli` and inspecting the various keys, in particular the hash entry.

```
> type key  # hash
> hvals key  # corresponding to hash
```

Check out the Redis commands available [here](http://redis.io/commands)


### Full Docker Container Support

If you are interested in using Docker, and running both the sample and Redis server inside containers that network with one another then:

Update `src/main/resources/application.properties` changing `spring.redis.host=localhost` to `spring.redis.host=redis-server`

Next, run:

`docker-compose build` followed by `docker-compose up` and then once completed go to go to [http://localhost:3099/login](http://localhost:3099/login).

Everything will run exactly as before, but the auth0 sample and the redis server are running in separate containers.
