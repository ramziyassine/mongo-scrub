#!/bin/bash
echo "Inserting data to db...."
mongo --port "$LOCAL_MONGO_PORT" /vagrant/pushData.js