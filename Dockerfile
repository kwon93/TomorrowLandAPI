FROM openjdk:17

WORKDIR /usr/src/app

ARG JAR_PATH=./build/libs

COPY ${JAR_PATH}/api-0.0.2-SNAPSHOT.jar api-0.0.2-SNAPSHOT.jar

CMD ["java", "-jar", "api-0.0.2-SNAPSHOT.jar"]
