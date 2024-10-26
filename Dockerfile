FROM tomcat:10.1.28
RUN rm -rf /usr/local/tomcat/webapps/*
COPY ROOT.war /usr/local/tomcat/webapps

ENV PORT 8080

EXPOSE 8080

CMD ["catalina.sh", "run"]