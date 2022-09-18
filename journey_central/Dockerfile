# actual container
FROM amazoncorretto:17-alpine3.15-full
ENV APP_HOME=/usr/app/
RUN apk add gcompat
ENV LD_PRELOAD=/lib/libgcompat.so.0

WORKDIR $APP_HOME
ARG JAR_FILE=build/libs/journey-0.0.1-SNAPSHOT.jar
COPY journey_central/${JAR_FILE} ./app.jar
COPY ./integrations ../integrations
COPY ./journey_central/treeassistant-tatan.json ./treeassistant-tatan.json
RUN apk add --update pulseaudio


EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]