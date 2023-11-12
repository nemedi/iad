FROM alpine
RUN apk --no-cache add openjdk19 mc net-tools iputils-ping
RUN mkdir -p /usr/share/loadbalancing/runtime
ADD ./lib/* /usr/share/loadbalancing/
ADD ./target/loadbalancing-backend-0.0.1-SNAPSHOT.jar /usr/share/loadbalancing/loadbalancing.jar
WORKDIR /usr/share/loadbalancing
ENV PORT 9090
ENV SERVER_ID Server
ENV FRONTEND_PORT 8080
ENV DEBUG "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=0.0.0.0:8000"
CMD ["sh", "-c", "java $DEBUG -jar loadbalancing.jar $PORT $SERVER_ID $FRONTEND_PORT"]