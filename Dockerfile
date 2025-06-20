# Sử dụng OpenJDK 21
FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY . .

RUN ./mvnw clean package -DskipTests

EXPOSE 8091

CMD ["java", "-jar", "target/hotel-booking-java-0.0.1-SNAPSHOT.jar"]
