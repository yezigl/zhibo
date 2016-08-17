#! /bin/bash

app=zhibo
module=zhibo
version=0.0.1
jar=$module-$version-SNAPSHOT.jar
pid=`ps -ef | grep $jar | grep -v grep | awk '{print $2}'`

webroot=/opt/$app
if [ ! -d $webroot ] ; then
    sudo mkdir $webroot && sudo chown -R ec2-user:ec2-user $webroot
fi

cd $webroot

if [ -n "$pid" ] ; then
    kill $pid
fi

if [ $app = $module ] ; then
    cp $WORKSPACE/target/$jar ./
else
    cp $WORKSPACE/$module/target/$jar ./
fi

nohup java -jar $jar &
echo $! > $module.pid