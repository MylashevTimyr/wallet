FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/WalletTest-0.0.1-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]
