FROM eclipse-temurin:17-jre
WORKDIR /app
COPY target/online-feedback-1.0.0.jar app.jar
EXPOSE 8080
ENV PORT=8080
CMD ["java", "-jar", "app.jar"]
