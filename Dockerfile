FROM openjdk:11

RUN apt update && apt -y upgrade
RUN apt install -y maven

WORKDIR /app

COPY pom.xml .
COPY ./src .

RUN mvn clean package

EXPOSE 8081/tcp 8082/tcp

CMD ["java", "-jar", "target/Watch-Dog-1.0-SNAPSHOT.jar"]
