# Sử dụng OpenJDK 21
FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY . .

RUN ./mvnw clean package -DskipTests

EXPOSE 8092

CMD ["java", "-jar", "target/hotel-booking-java-0.0.1-SNAPSHOT.jar", "--spring.application.name=hotel-booking-java", "--server.port=8092", "--server.address=0.0.0.0"]

#CMD ["java", "-jar", "target/hotel-booking-java-0.0.1-SNAPSHOT.jar"]
