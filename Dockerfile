FROM maven:3.9.6-amazoncorretto-21-al2023 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package
EXPOSE 8080
ENTRYPOINT ["java","-jar","/home/app/target/watchlist-service.jar"]