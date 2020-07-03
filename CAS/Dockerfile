
FROM tomcat:8.5.55-jdk8-openjdk
MAINTAINER "Sichun Qin  sichunqin@hotmail.com"
 
ADD server.xml /usr/local/tomcat/conf/
ADD cas-tomcat-keystore.p12 /usr/local/tomcat/keystore/

ADD cas.war /usr/local/tomcat/webapps/
EXPOSE 8443
EXPOSE 8080

CMD ["catalina.sh", "run"]