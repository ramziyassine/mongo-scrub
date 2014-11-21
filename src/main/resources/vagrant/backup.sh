#!/bin/bash

## backup runs on the local server 
export HOST=127.0.0.1
export DUMP_DESTINATION=/backup/
export DIRECTORY=`date +%Y-%m-%d_%H_%M_%S`
export SCRUBBED_DIRECTORY=/backup-scrubbed/

## Backup Stage

if [ ! -d  "$DUMP_DESTINATION/$DIRECTORY" ]; then
    mkdir $DUMP_DESTINATION/$DIRECTORY
fi

######## The dump stage
mongodump -h $HOST --port $LOCAL_MONGO_PORT -o $DUMP_DESTINATION/$DIRECTORY


####### The scrub stage
cp -R $DUMP_DESTINATION$DIRECTORY  $SCRUBBED_DIRECTORY$DIRECTORY

cd $SCRUBBED_DIRECTORY$DIRECTORY

find . -name "*.bson" | grep -v ".indexes"  | while read f; do
    echo "Attempting to scrub $file"
    java -jar /vagrant/hudl-utils.jar -b "$f"
done

find . -name "*-scrubbed.bson" | while read f; do
    mv "$f" "${f//-scrubbed/}"
done


echo "Backup created at $DUMP_DESTINATION$DIRECTORY"
echo "Scrubbed Backup created at $SCRUBBED_DIRECTORY$DIRECTORY"
