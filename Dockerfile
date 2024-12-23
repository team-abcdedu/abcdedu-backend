FROM openjdk:17-alpine

RUN apk add --no-cache fontconfig ttf-dejavu && fc-cache -fv

COPY build/libs/abcdedu-server.jar app.jar

CMD ["java", "-Djava.awt.headless=true", "-jar", "app.jar"]