FROM adoptopenjdk:16

ENV TZ="Europe/Samara"
RUN mkdir /app
COPY ./build /app

CMD ["java" , "-jar",  "/app/libs/vekselyator-1.0.jar"]
