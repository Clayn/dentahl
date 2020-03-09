#!/bin/sh
JAVA_EXEC=java

if [ ! -f java.exec ]; then
   echo "Using install Java: $(which java)"
else
   echo "Setting Java to: $(cat java.exec)"
   JAVA_EXEC=$(cat java.exec)
fi

if [ ! -f dentahl.pid ]; then
   echo "Starting Dentahl"
   nohup ${JAVA_EXEC} -jar dentahl4j-server-0.2.0-SNAPSHOT.jar > dentahl.launch 2>&1 &
else
   echo "Dentahl is already running with PID $(cat dentahl.pid)"
fi