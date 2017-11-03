FROM gradle:4.2-jdk8

WORKDIR /tmp
ADD . /tmp

RUN gradle build

CMD java -jar build/libs/auth0-spring-security-mvc.war
EXPOSE 8080
