FROM openjdk:17-alpine
COPY build/libs/abcdedu-server.jar app.jar
CMD ["java", "-jar", "app.jar"]