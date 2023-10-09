FROM openjdk:17
COPY target/*.jar app.jar
COPY ./images /images
ENTRYPOINT ["java","-jar","/app.jar"]