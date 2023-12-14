FROM openjdk:17-oracle

WORKDIR /app

COPY build/libs/spring-course-second-practice-0.0.1-SNAPSHOT.jar app.jar
COPY src/main/resources/application.yaml application.yaml
COPY src/main/resources/students_mock.csv students_mock.csv

ENV SPRING_PROFILES_ACTIVE=prod
ENV APP_LISTENING_ENABLE=true
ENV APP_UPLOADING_ENABLE=true
ENV APP_UPLOADING_PATH=students_mock.csv


CMD ["java", "-jar", "app.jar"]