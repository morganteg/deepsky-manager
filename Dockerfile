FROM mysql:8.0

ENV MYSQL_ROOT_PASSWORD=root
ENV MYSQL_DATABASE=deepskymanager

#CMD ["java", "-cp", "/app/app.jar", "com.examples.hellodocker.App"]