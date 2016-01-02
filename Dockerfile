FROM azul/zulu-openjdk-centos:8
MAINTAINER Kim Christian Gaarder <kim.christian.gaarder@gmail.com>
RUN yum -y install yum-cron
RUN yum -y update
RUN yum -y install curl

# Install Application
RUN adduser hellouser
ADD target/dropwizard-drag-drop-connect-application-*.jar /home/hellouser/dropwizard-drag-drop-connect-application.jar"
ADD docker/drag-drop-connect.yml /home/hellouser/drag-drop-connect.yml
RUN chown hellouser:hellouser /home/hellouser/drag-drop-connect.yml
ADD docker/drag-drop-connect_override.properties /home/hellouser/drag-drop-connect-override.properties
RUN chown hellouser:hellouser /home/hellouser/drag-drop-connect-override.properties

EXPOSE 21500:21599

WORKDIR "/home/hellouser"
CMD [ \
    "java", \
    "-Xdebug", \
    "-Xrunjdwp:transport=dt_socket,address=21515,server=y,suspend=n", \
    "-Dcom.sun.management.jmxremote.port=21516", \
    "-Dcom.sun.management.jmxremote.rmi.port=21516", \
    "-Dcom.sun.management.jmxremote.ssl=false", \
    "-Dcom.sun.management.jmxremote.local.only=false", \
    "-Dcom.sun.management.jmxremote.authenticate=false", \
    "-Djava.rmi.server.hostname=localhost", \
    "-jar", \
    "dropwizard-drag-drop-connect-application.jar" \
]
