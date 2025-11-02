FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/java-kanban6-1.0.0.jar app.jar
CMD ["sh", "-c", "java -jar app.jar && tail -f /dev/null"]