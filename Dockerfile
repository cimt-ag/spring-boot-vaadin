FROM alpine/java:21-jdk
WORKDIR /
ADD build/libs/spring-boot-vaadin-0.1.0-SNAPSHOT.jar app.jar
EXPOSE 8800
CMD java -jar -Dspring.profiles.active=prod -Dvaadin.productionMode=true app.jar
