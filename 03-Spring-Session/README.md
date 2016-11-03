# Spring Session example

This is a simple Java Spring MVC web application that is meant to be used as the starting point for [Auth0 Java Spring MVC Quickstart](https://auth0.com/docs/quickstart/webapp/java-spring-mvc). 

However, this sample builds on the Login Example by illustrating how to easily integrate with [Spring Session](http://projects.spring.io/spring-session/)

Since this sample is a Spring Boot application, it follows the [Spring Session for Spring Boot instructions](http://docs.spring.io/spring-session/docs/current/reference/html5/guides/boot.html#boot-sample)

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

## Configure your Spring Security app

Your Spring Security app needs some information in order to authenticate against your Auth0 account. We have created a file for you but you need to update some of the entries with the valid values for your Client. The file is `/src/main/resources/auth0.properties.example` and it contains the following:

```
auth0.domain: {DOMAIN}
auth0.issuer: {ISSUER}
auth0.clientId: {CLIENT_ID}
auth0.clientSecret: {CLIENT_SECRET}
auth0.onLogoutRedirectTo: /login
auth0.securedRoute: /portal/*
auth0.loginCallback: /callback
auth0.loginRedirectOnSuccess: /portal/home
auth0.loginRedirectOnFail: /login
auth0.base64EncodedSecret: true
auth0.authorityStrategy: ROLES
auth0.defaultAuth0WebSecurityEnabled: false
auth0.connection: {CONNECTION}
auth0.customLogin: true
auth0.signingAlgorithm: HS256
#auth0.signingAlgorithm: RS256
#auth0.publicKeyPath: /WEB-INF/certificate/cert.pem
```

Rename the file to `auth0.properties` and change the following values:
- `auth0.domain`:	Your auth0 domain. You can find the correct value on the Settings tab of your client on the dashboard.
- `auth0.issuer`:	The issuer of the JWT Token. This is typically your auth0 domain with a `https://` prefix and a `/` suffix. For example, if your `auth0.domain` is `example.auth0.com` then the `auth0.issuer` should be set to `https://example.auth0.com/` (the trailing slash is important!).
- `auth0.clientId`:	The unique identifier for your client. You can find the correct value on the Settings tab of your client on the dashboard. 
- `auth0.clientSecret`:	The secret used to sign and validate the tokens that will be used in the different authentication flows. You can find the correct value on the Settings tab of your client on the dashboard.
- `auth0.connection`: The name of the database connection you created, for example `custom-login-DB`.
- `auth0.customLogin`: Set to `true` to enable custom login instead of Lock.


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

`docker-compose build` followed by `docker-compose up` and then once completed go to [http://localhost:3099/login](http://localhost:3099/login).

Everything will run exactly as before, but the auth0 sample and the redis server are running in separate containers.
