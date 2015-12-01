#! /bin/bash
if [ $# -lt 1 ]; then
	echo "please input which app"
	exit 1
fi
JAVA_HOME=/opt/soft/java
APP_PATH=/opt/apps
APP_NAME=$1
# check tools.jar
 
javacount=`ps -ef|grep java|grep $APP_NAME |wc -l`

DIR="$APP_PATH"/app/"$APP_NAME"
PROGNAME=`basename $0`
ROOT_PATH="$DIR"
echo $ROOT_PATH
PID_FILE="$APP_PATH"/temp/pid/"$APP_NAME"
if [ $javacount -ge 1 ] ; then
  echo "begin to kill"
	if [ ! -e $PID_FILE ]; then
		echo "pid file($PID_FILE) not exits"
		exit 1
	fi
	echo "kill -12 'cat $PID_FILE'"
	kill -12 `cat $PID_FILE`
	sleep 2
	kill -9 `cat $PID_FILE`
	echo "kill -9 `cat $PID_FILE`"
	rm -rf $PID_FILE
fi
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

JAVA_OPTS="-Xms$VM_XMS -Xmx$VM_XMX -Xmn$VM_XMN -Xss1024K -XX:PermSize=128m
-XX:MaxPermSize=256m"
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
#echo "clasPath"
#echo $EXEC_PATH
MAIN_CLASS_PATH=$ROOT_PATH/lib/com.bj58.zhaopin.app.engine-0.0.1-SNAPSHOT.jar
#echo $MAIN_CLASS_PATH
MAIN_CLASS=com.bj58.zhaopin.app.engine.bootstrap.Main
$JAVA_HOME/bin/java  $JAVA_OPTS -classpath $CLASS_PATH:$EXEC_PATH:$MAIN_CLASS_PATH -Duser.dir=$ROOT_PATH/conf -Dapp.dir=$APP_PATH  $MAIN_CLASS -Dapp.name=$APP_NAME &
#echo $!
echo $! > $PID_FILE
