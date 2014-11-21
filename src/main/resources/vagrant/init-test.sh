#!/bin/bash
## This is where as an enhancement I would think of using tools like Chef!!
export LOCAL_MONGO_PORT=4816
export FOREIGN_MONGO_PORT=4815
export LOCAL_IP=192.168.4.16
export FOREIGN_IP=192.168.4.15

sudo touch /etc/default/mongod
sudo chmod a+w /etc/default/mongod
sudo echo "export MONGOD_CONFIG_HOME=/vagrant/mongo-test.conf" > /etc/default/mongod
sudo echo "export CONF=\$MONGOD_CONFIG_HOME" >> /etc/default/mongod

