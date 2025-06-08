#stage 1
FROM alpine:3.20 AS base
RUN apk add openjdk21
#Maintainer Info
LABEL maintainer="alex lakers <https://github.com/AlexLakers>"
ARG JAR_NAME
ARG JAR_DIR
ARG FULL_PATH=${JAR_DIR}/${JAR_NAME}
# Add the application's jar to the container
COPY ${FULL_PATH} app.jar
#unpackage jar file
RUN mkdir -p ${JAR_DIR}/application \
   && cd ${JAR_DIR}/application \
   && jar -xf /app.jar
#stage 2
FROM alpine:3.20 AS result
ARG JAR_DIR
ARG JAR_PATH=${JAR_DIR}/application
RUN apk add openjdk21
COPY --from=base ${JAR_PATH}/BOOT-INF/lib /app/lib
COPY --from=base ${JAR_PATH}/META-INF /app/META-INF
COPY --from=base ${JAR_PATH}/BOOT-INF/classes /app

#execute the application

ENTRYPOINT ["java", "-cp","app:app/lib/*","com.alex.web.microservices.songs.lib.songs.SongsServiceApplicationRunner"]
EXPOSE 8086
