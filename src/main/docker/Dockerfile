FROM openjdk:8-jre-alpine

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    desyuser_SLEEP=0 \
    JAVA_OPTS=""

# Add a desyuser user to run our application so that it doesn't need to run as root
RUN adduser -D -s /bin/sh desyuser
WORKDIR /home/desyuser

ADD entrypoint.sh entrypoint.sh
RUN chmod 755 entrypoint.sh && chown desyuser:desyuser entrypoint.sh
USER desyuser

ENTRYPOINT ["./entrypoint.sh"]

EXPOSE 8181 5701/udp

ADD *.war app.war

