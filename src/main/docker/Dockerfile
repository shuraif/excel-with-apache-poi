FROM openjdk:8-jdk-alpine
RUN apk add --no-cache bash
RUN apk add --update \
    curl \
    && rm -rf /var/cache/apk/*
VOLUME /tmp
ADD claims-delta-load-0.0.1.jar app.jar
RUN bash -c 'touch /app.jar'
EXPOSE 8147
ENTRYPOINT ["java","-jar","/app.jar"]
