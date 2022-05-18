FROM adoptopenjdk/openjdk11
COPY ./src/main/resources/* /resources
COPY ./target/*.jar /authentication.jar
ENTRYPOINT ["java","-jar","/authentication.jar"]
EXPOSE 6060