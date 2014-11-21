#!/bin/bash
## This is where as an enhancement I would think of using tools like Chef!!

sudo touch ~/.bash_aliases
sudo chmod a+w ~/.bash_aliases

sudo echo "export LOCAL_MONGO_PORT=4816" > ~/.bash_aliases
sudo echo "export FOREIGN_MONGO_PORT=4815" >> ~/.bash_aliases
sudo echo "export LOCAL_IP=192.168.4.16" >> ~/.bash_aliases
sudo echo "export FOREIGN_IP=192.168.4.15" >> ~/.bash_aliases

sudo touch /etc/default/mongod
sudo chmod a+w /etc/default/mongod
sudo echo "export MONGOD_CONFIG_HOME=/vagrant/mongo-test.conf" > /etc/default/mongod
sudo echo "export CONF=\$MONGOD_CONFIG_HOME" >> /etc/default/mongod

. ~/.bash_aliases
