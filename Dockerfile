FROM openjdk:17-jdk

WORKDIR /app

COPY mvnw mvnw.cmd pom.xml ./

COPY src ./src

RUN mvn clean package -DskipTests=true

CMD ["./mvnw", "spring-boot:run"]