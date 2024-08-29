# 빌드 스테이지
FROM gradle:7.6.1-jdk17 AS build
WORKDIR /home/gradle/src
COPY . .
RUN gradle build --no-daemon

# 실행 스테이지
FROM openjdk:17-slim

WORKDIR /app

# 빌드 결과물 복사
COPY --from=build /home/gradle/src/build/libs/*.jar app.jar

# 환경 변수 설정 (예시)
ENV SPRING_PROFILES_ACTIVE=production

ENTRYPOINT ["java", "-jar", "app.jar"]