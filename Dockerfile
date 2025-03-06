# Sử dụng OpenJDK 21
FROM eclipse-temurin:21-jdk

# Đặt thư mục làm việc trong container
WORKDIR /app

# Copy file JAR từ thư mục target vào container
COPY target/hotel-booking-java-0.0.1-SNAPSHOT.jar app.jar

# Cấu hình để chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]
