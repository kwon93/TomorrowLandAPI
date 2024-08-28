# 빌드 스테이지
FROM gradle:7.2-jdk17 AS build

WORKDIR /home/gradle/src

# Gradle 사용자에게 적절한 권한 부여
USER root
RUN mkdir -p /home/gradle/.gradle && chown -R gradle:gradle /home/gradle/.gradle
USER gradle

# 의존성 먼저 복사 및 다운로드
COPY --chown=gradle:gradle build.gradle settings.gradle ./
RUN gradle dependencies --no-daemon --stacktrace --info

# 소스 코드 복사
COPY --chown=gradle:gradle . .

# 빌드 실행
RUN gradle build --no-daemon --stacktrace --info

# 실행 스테이지
FROM openjdk:17-slim

WORKDIR /app

# 빌드 결과물 복사
COPY --from=build /home/gradle/src/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
