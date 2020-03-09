#!/bin/sh

if [ ! -f dentahl.pid ]; then
   echo "No PID file found"
else

   PID=$(cat dentahl.pid)
   echo "Found Dentahl running with PID: ${PID}"
   kill -15 ${PID}
fi