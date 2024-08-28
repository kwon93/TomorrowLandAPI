# 빌드 스테이지
FROM gradle:7.2-jdk17 AS build

WORKDIR /home/gradle/src

# 의존성 먼저 복사 및 다운로드
COPY build.gradle settings.gradle ./
RUN gradle dependencies --no-daemon

# 소스 코드 복사
COPY . .

# 빌드 실행
RUN gradle build --no-daemon --info

# 실행 스테이지
FROM openjdk:17-slim

WORKDIR /app

# 빌드 결과물 복사
COPY --from=build /home/gradle/src/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
