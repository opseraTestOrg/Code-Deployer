FROM gradle:4.10-jdk8-alpine AS build
ENV DOCKER_ENV=dev
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle clean build --no-daemon

FROM openjdk:8-jre-slim
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/spring-boot-application.jar
EXPOSE 8077
ENTRYPOINT ["java", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", "-Dspring.profiles.active=$DOCKER_ENV", "-Djava.security.egd=file:/dev/./urandom","-jar", "/app/spring-boot-application.jar"]