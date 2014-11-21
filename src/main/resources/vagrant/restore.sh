#!/bin/bash

export DUMP_DESTINATION=/backup-scrubbed/

cd $DUMP_DESTINATION
export LATEST_DUMP=`ls -Art | tail -n 1`

mongorestore --host "$FOREIGN_IP" --port "$FOREIGN_MONGO_PORT" "$DUMP_DESTINATION$LATEST_DUMP"