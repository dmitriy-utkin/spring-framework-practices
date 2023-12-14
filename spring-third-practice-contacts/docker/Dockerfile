FROM openjdk:17-oracle

WORKDIR /app

COPY ../build/libs/spring-third-practice-contacts-0.0.1-SNAPSHOT.jar app.jar
COPY ../src/main/resources/application.yaml application.yaml
COPY ../src/main/resources/emails.csv emails.csv
COPY ../src/main/resources/first-names.csv first-names.csv
COPY ../src/main/resources/last-names.csv last-names.csv
COPY ../src/main/resources/phones.csv phones.csv

ENV SPRING_PROFILES_ACTIVE=default
ENV APP_UPLOADING_ENABLE=true
ENV DEFAULT_NUMBER_GEN_CONTACT=10
ENV PSQL_USER=postgres
ENV PSQL_PASS=postgres
ENV PSQL_URL=jdbc:postgresql://localhost:5432/contacts
ENV PSQL_SCHEMA=contacts_schema



CMD ["java", "-jar", "app.jar"]