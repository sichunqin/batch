#FROM openjdk:8
FROM openjdk:8
VOLUME /Uplod

ADD portal-0.0.1-SNAPSHOT.jar /app.jar
COPY Daxin_Upload /Daxin_Upload
COPY resources /resources
COPY model /model
COPY jfxrt.jar /usr/local/openjdk-8/jre/lib/ext/
COPY cas-tomcat.cer /
RUN keytool -import --alias cas-tomcat -file cas-tomcat.cer -storepass changeit -keystore /usr/local/openjdk-8/jre/lib/security/cacerts -noprompt

EXPOSE 8080

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","-Dspring.profiles.active=docker","/app.jar"]