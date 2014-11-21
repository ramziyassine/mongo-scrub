#!/bin/bash
## This is where as an enhancement I would think of using tools like Chef!!
sudo cp /vagrant/test-prod.txt ~/.bash_aliases


sudo touch /etc/default/mongod
sudo chmod a+w /etc/default/mongod
sudo echo "export MONGOD_CONFIG_HOME=/vagrant/mongo-prod.conf" > /etc/default/mongod
sudo echo "export CONF=\$MONGOD_CONFIG_HOME" >> /etc/default/mongod
export LOCAL_MONGO_PORT=4815
export FOREIGN_MONGO_PORT=4816
export LOCAL_IP=192.168.4.15
export FOREIGN_IP=92.168.4.16

. ~/.bash_aliases