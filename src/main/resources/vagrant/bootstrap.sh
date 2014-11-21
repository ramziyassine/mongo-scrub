#!/bin/bash

chmod a+w -R /vagrant
##Followed the instructions from http://docs.mongodb.org/manual/tutorial/install-mongodb-on-ubuntu/
sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 7F0CEB10
echo 'deb http://downloads-distro.mongodb.org/repo/ubuntu-upstart dist 10gen' | sudo tee /etc/apt/sources.list.d/mongodb.list
sudo apt-get update
sudo apt-get install -y mongodb-org 
sudo mkdir /backup 
sudo chmod a+w -R /backup
sudo mkdir /backup-scrubbed
sudo chmod a+w -R /backup-scrubbed/
crontab /vagrant/schedule_jobs.txt
/vagrant/installJava.sh