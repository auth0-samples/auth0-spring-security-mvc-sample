FROM maven:3.3-jdk-8
EXPOSE 3099
RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app
VOLUME /usr/src/app
ENTRYPOINT ["mvn", "clean", "compile", "exec:java"]
