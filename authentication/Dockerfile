FROM adoptopenjdk/openjdk11
WORKDIR /authentication
COPY ./src/main/resources/* resources/
COPY ./target/*.jar /authentication.jar
ENTRYPOINT ["java","-jar", "-Dspring.config.location=resources/application.properties","/authentication.jar"]
EXPOSE 6060