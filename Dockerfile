FROM maven:3.9.6-eclipse-temurin-17 AS build
ENV PORT=8081
COPY . /app

WORKDIR /app

COPY src ./src

RUN mvn -B -DskipTests clean package

CMD ["mvn", "spring-boot:run"]