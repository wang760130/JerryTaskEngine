#! /bin/bash
if [ $# -lt 1 ]; then
	echo "please input which app"
	exit 1
fi
JAVA_HOME=/opt/zengnz/jdk1.7.0_51
APP_PATH=/opt/apps
APP_NAME=$1
# check tools.jar
if [ ! -f "$JAVA_HOME"/lib/tools.jar ]; then
  echo "Can't find tools.jar in JAVA_HOME"
  echo "Need a JDK to run javac"
  exit 1
fi
javacount=`ps -ef|grep java|grep $APP_NAME |wc -l`
if [ $javacount -ge 1 ] ; then
  echo "warning: has a [$APP_NAME] is running, please
check......................................"
  exit 1
fi
DIR=/opt/apps/app/$APP_NAME
PID_FILE="$APP_PATH"/temp/pid/"$APP_NAME"
if [ -e $PID_FILE ]; then
  PID_INFO=`cat $PID_FILE`
  SERVICE_PID=`ps -ef | grep -v grep | grep $PID_INFO | sed -n  '1P' | awk '{print $2}'` 
  echo "ps -ef | grep -v grep | grep $PID_INFO | sed -n  '1P' | awk '{print $2}'"
  echo $SERVICE_PID
  if [ $SERVICE_PID ]; then
    echo "startup fail! Please close the service after to restart!" 
    echo `date` +"[$SERVICE_NAME] is running" >> ../log/monitor.log
    exit 1 
  else 
    echo "This service will startup!"
    echo `date` +"[$SERVICE_NAME] is starting" >> ../log/monitor.log
  fi
fi

PROGNAME=`basename $0`
ROOT_PATH="$DIR"
echo $ROOT_PATH

JVM_PATH="$DIR"/conf/app_config.xml
echo $JVM_PATH
VM_XMS=`cat $JVM_PATH | sed 's#\(.*\)>\(.*\)#\1>#g' |awk '/vm.xms/{getline;print}' | sed 's#<value>\(.*\)</value>#\1#g'|sed 's/ //g'`
VM_XMX=`cat $JVM_PATH | sed 's#\(.*\)>\(.*\)#\1>#g' |awk '/vm.xmx/{getline;print}' | sed 's#<value>\(.*\)</value>#\1#g'|sed 's/ //g'`
VM_XMN=`cat $JVM_PATH | sed 's#\(.*\)>\(.*\)#\1>#g' |awk '/vm.xmn/{getline;print}' | sed 's#<value>\(.*\)</value>#\1#g'|sed 's/ //g'`
# java opts
if [ "$VM_XMS" = "" ]; then
  VM_XMS=256m
fi

if [ "$VM_XMX" = "" ]; then
  VM_XMX=256m
fi

if [ "$VM_XMN" = "" ]; then
  VM_XMN=128m
fi

JAVA_OPTS="-Xms$VM_XMS -Xmx$VM_XMX -Xmn$VM_XMN -Xss1024K -XX:PermSize=64m
-XX:MaxPermSize=128m -XX:ParallelGCThreads=2 -XX:+UseConcMarkSweepGC
-XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:+UseCMSCompactAtFullCollection"
echo $JAVA_OPTS

# class path
CLASS_PATH="$ROOT_PATH":"$JAVA_HOME"/lib/tools.jar

for jar in $APP_PATH/lib/*.jar; do
  CLASS_PATH=$CLASS_PATH:$jar
done
EXEC_PATH="$ROOT_PATH"
for jar in $ROOT_PATH/*.jar; do
  EXEC_PATH=$EXEC_PATH:$jar
done
#echo $CLASS_PATH
EXEC_PATH=$EXEC_PATH:$ROOT_PATH
echo "clasPath"
echo $EXEC_PATH
#echo "root_path"
#echo $ROOT_PATH
MAIN_CLASS_PATH=$ROOT_PATH/lib/com.bj58.zhaopin.app.engine-0.0.1-SNAPSHOT.jar
MAIN_CLASS=com.bj58.zhaopin.app.engine.bootstrap.Main
#echo  $CLASS_PATH:$EXEC_PATH:$MAIN_CLASS_PATH
$JAVA_HOME/bin/java $JAVA_OPTS -classpath $CLASS_PATH:$EXEC_PATH:$MAIN_CLASS_PATH -Duser.dir=$ROOT_PATH/conf -Dapp.dir=$APP_PATH  $MAIN_CLASS -Dapp.name=$APP_NAME &
#echo $!
echo $$ > $PID_FILE
