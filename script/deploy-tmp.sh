#! /bin/bash

app=zhibo
module=zhibo
version=0.0.1
jar=$module-$version-SNAPSHOT.jar
pid=`ps -ef | grep $jar | grep -v grep | awk '{print $2}'`

webroot=/opt/webapps/$app
if [ ! -d $webroot ] ; then
    sudo mkdir $webroot && sudo chown -R devel:devel $webroot
fi

cd $webroot

if [ -n "$pid" ] ; then
    kill $pid
fi

cp /tmp/$app/target/$jar ./

nohup java -jar $jar > /dev/null 2>&1 &
echo $! > $module.pid