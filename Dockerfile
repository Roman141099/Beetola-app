FROM adoptopenjdk/openjdk11
RUN mkdir /app
COPY build/libs/betoola-app-1.0.0.jar /app
WORKDIR /app
EXPOSE 8080
CMD ["java", "-jar", "betoola-app-1.0.0.jar"]