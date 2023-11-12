FROM alpine
RUN apk --no-cache add openjdk19 mc net-tools iputils-ping
RUN mkdir -p /usr/share/loadbalancing/runtime
ADD ./lib/* /usr/share/loadbalancing/
ADD ./target/loadbalancing-frontend-0.0.1-SNAPSHOT.jar /usr/share/loadbalancing/loadbalancing.jar
WORKDIR /usr/share/loadbalancing
ENV PORT 8080
ENV BACKENDS ""
CMD ["sh", "-c", "java -jar loadbalancing.jar $PORT $BACKENDS"]