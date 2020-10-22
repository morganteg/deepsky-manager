FROM tomcat:latest

RUN java -version

COPY /target/deepsky-manager-0.0.1-SNAPSHOT.jar /app/app.jar

CMD ["java", "-jar", "/app/app.jar"]