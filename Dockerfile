FROM adoptopenjdk:16

RUN mkdir /app
COPY ./build /app

CMD ["java" , "-jar",  "/app/libs/vekselyator-1.0.jar"]
