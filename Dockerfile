FROM openjdk:17-jdk-alpine
ADD /target/ya_school-0.0.1-SNAPSHOT.jar RestApi.jar
ENTRYPOINT ["java", "-jar", "RestApi.jar"]