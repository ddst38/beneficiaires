FROM openjdk:17-jdk-alpine as builder

WORKDIR /app
COPY . /app

RUN ./mvnw -s settings.xml clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine

COPY --from=builder /app/target/beneficiaires-1.0.jar /beneficiaires-1.0.jar

CMD ["java", "-jar", "/beneficiaires-1.0.jar"]
