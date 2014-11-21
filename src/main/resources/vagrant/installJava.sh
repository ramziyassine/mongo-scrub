#!/bin/sh


export MAIN_LINK="http://download.oracle.com/otn-pub/java/jdk/8u25-b17/jdk-8u25-linux-x64.tar.gz"
export TAR_FILE="jdk-8u25-linux-x64.tar.gz"
export JVM_MAIN="jdk1.8.0_25"
sudo apt-get install curl -y 

curl -L --cookie "oraclelicense=accept-securebackup-cookie" $MAIN_LINK -o $TAR_FILE  
tar -xvf $TAR_FILE

sudo mkdir -p /usr/lib/jvm
sudo mv ./jdk1.8.* /usr/lib/jvm/


sudo update-alternatives --install "/usr/bin/java" "java" "/usr/lib/jvm/$JVM_MAIN/bin/java" 1
sudo update-alternatives --install "/usr/bin/javac" "javac" "/usr/lib/jvm/$JVM_MAIN/bin/javac" 1
sudo update-alternatives --install "/usr/bin/javaws" "javaws" "/usr/lib/jvm/$JVM_MAIN/bin/javaws" 1



sudo chmod a+x /usr/bin/java 
sudo chmod a+x /usr/bin/javac 
sudo chmod a+x /usr/bin/javaws
sudo chown -R root:root /usr/lib/jvm/$JVM_MAIN

rm $TAR_FILE

java -version
