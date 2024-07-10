FROM openjdk:17
COPY /Users/nicoleanderson/Desktop/STS_WORKSPACE_DARK/TaskManagerAPI/target/TaskManagerAPI-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
WORKDIR /app
CMD java -jar /TaskManagerAPI-0.0.1-SNAPSHOT.jar

