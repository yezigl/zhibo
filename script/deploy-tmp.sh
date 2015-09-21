#! /bin/bash

app=zhibo
module=zhibo
version=0.0.1
jar=$module-$version-SNAPSHOT.jar
pid=`ps -ef | grep $jar | grep -v grep | awk '{print $2}'`

cd /opt/$app

if [ -n "$pid" ] ; then
    kill $pid
fi

cp /tmp/$jar ./

nohup java -jar $jar &
echo $! > $module.pid