# Sử dụng OpenJDK 21
FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY . .

RUN ./mvnw clean package -DskipTests

CMD ["java", "-jar", "target/hotel-booking-java-0.0.1-SNAPSHOT.jar"]
