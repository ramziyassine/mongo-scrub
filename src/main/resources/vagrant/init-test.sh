#!/bin/bash
## This is where as an enhancement I would think of using tools like Chef!!
sudo cp /vagrant/test-bash.txt ~/.bash_aliases


sudo touch /etc/default/mongod
sudo chmod a+w /etc/default/mongod
sudo echo "export MONGOD_CONFIG_HOME=/vagrant/mongo-test.conf" > /etc/default/mongod
sudo echo "export CONF=\$MONGOD_CONFIG_HOME" >> /etc/default/mongod

