FROM adoptopenjdk/openjdk11:x86_64-ubuntu-jdk-11.0.3_7
RUN apt-get update && apt-get install -y net-tools iputils-ping mc
RUN mkdir -p /usr/share/loadbalancing-frontend
RUN mkdir -p /usr/share/loadbalancing-frontend/data
ADD ./lib/* /usr/share/loadbalancing-frontend/
ADD ./target/loadbalancing-frontend-0.0.1-SNAPSHOT.jar /usr/share/loadbalancing-frontend/loadbalancing-frontend.jar
WORKDIR /usr/share/loadbalancing-frontend
ENV PORT 8080
ENV BACKENDS ""
ENV DEBUG "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=0.0.0.0:8000"
CMD ["sh", "-c", "/opt/java/openjdk/bin/java $DEBUG -jar loadbalancing-frontend.jar $PORT $BACKENDS"]