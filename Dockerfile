# 빌드 스테이지
FROM gradle:7.2-jdk17 AS build
WORKDIR /home/gradle/src
COPY --chown=gradle:gradle . .
RUN gradle build asciidoctor --no-daemon

# 실행 스테이지
FROM openjdk:17-slim

WORKDIR /usr/src/app

COPY --from=build /home/gradle/src/build/libs/*.jar ./app.jar
COPY --from=build /home/gradle/src/build/docs/asciidoc /usr/src/docs

CMD ["java", "-jar", "app.jar"]
