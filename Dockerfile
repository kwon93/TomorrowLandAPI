# 빌드 스테이지
FROM gradle:7.2-jdk17 AS build
WORKDIR /home/gradle/src
COPY --chown=gradle:gradle . .
RUN gradle build

# 실행 스테이지
FROM openjdk:17-slim

WORKDIR /usr/src/app

COPY --from=build /home/gradle/src/build/libs/*.jar ./app.jar

CMD ["java", "-jar", "app.jar"]
