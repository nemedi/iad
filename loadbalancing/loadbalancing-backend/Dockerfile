FROM adoptopenjdk/openjdk11:x86_64-ubuntu-jdk-11.0.3_7
RUN apt-get update && apt-get install -y net-tools iputils-ping mc
RUN mkdir -p /usr/share/loadbalancing-backend
RUN mkdir /usr/share/loadbalancing-backend/transformations
ADD ./lib/* /usr/share/loadbalancing-backend/
ADD ./target/loadbalancing-backend-0.0.1-SNAPSHOT.jar /usr/share/loadbalancing-backend/loadbalancing-backend.jar
WORKDIR /usr/share/loadbalancing-backend
ENV PORT 9090
ENV SERVER_ID Server
ENV FRONTEND_HOST Frontend
ENV FRONTEND_PORT 8080
ENV DEBUG "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=0.0.0.0:8000"
CMD ["sh", "-c", "/opt/java/openjdk/bin/java $DEBUG -jar loadbalancing-backend.jar $PORT $ID"]