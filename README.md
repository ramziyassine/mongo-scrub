mongo-scrub
===========

### Introduction 
I am hoping to share with you what I have learned in 4-7 days around Vagrant and MongoDb. You should be able to run this on your machine.

### What is the big deal 
Following these instruction will show you how to run 2 different VMs on your machine, one will be a production VM and the other will be a test VM.
Both are running a mongodb server. In addition, I will show you how to backup one and push a scrubbed copy to the other machine.

### Prerequisites 

* Please [Visit the download page to install Vagrant](https://www.vagrantup.com/downloads.html) and use the appropriate download for your system.
* If you are using a Windows machine please know that it does not come with a native ssh client so:
    * You will need to install [PuTTY](http://the.earth.li/~sgtatham/putty/latest/x86/putty.exe)
    * You will also need to install [PuTTYgen](http://the.earth.li/~sgtatham/putty/latest/x86/puttygen.exe)
* [Download git!!!](http://git-scm.com/downloads)
* Once you have downloaded the software above please head to your **terminal**(Unix, Linux, OS X) or the **command prompt**(Windows).
* I recommend you run the commands below in your home directory, or a directory you have read/write access to.

Downloading the code
--------------------
    $ git clone https://github.com/ramziyassine/mongo-scrub.git
    $ cd mongo-scrub/src/main/resources/vagrant/


### Guided Tour

Follow these steps

Create the VMs
--------------
    $ vagrant up
I do apologize this is my first experience with vagrant and I could have packaged an already made box instead I am a using a bootsrap.sh script to install the needed software.
This will take a while until both VMs are up.
This is a good time for a ping pong match (maybe one set) or cup of coffee!!

### What happened

 Two virtual machines have been created for you and they are ready to roll.

|Name | IP address  | Mongodb server port
|----|---------- |----------------------
|prod | 192.168.4.15| 4815
|test | 192.168.4.16| 4816

* The boxes already have *java 8* installed (For the scrubber program)
* Mongodb server
* Equipped with utilities (bash scripts) to conduct the backup and restoration.


Let us Populate the Production database with some data
--------------
    $ vagrant ssh prod
This step on Windows will need to be done using putty. [Please check this URL, it is a good resource to see how?](http://stackoverflow.com/questions/9885108/ssh-to-vagrant-box-in-windows).
You can still run the command above in the prompt and vagrant will tell you what to do.

Generate
--------------
    vagrant@prod:~$ /vagrant/generateData.sh
You can execute the bash script above on the production server and it will populate the database with 100 users.

Take a peek
--------------
    vagrant@prod:~$ mongo --port $LOCAL_MONGO_PORT

The vagrant file calls for the help of an init bash script to setup the environment variables like **LOCAL_MONGO_PORT** In real scenarios I would try to look into [Chef](https://www.getchef.com/chef/).

    > db.users.find();

* Data has been populate to our users database (Type the above in the Mongo shell)
* Let us see how we can back up/scrub the data?
* Let us see how we can push the scrubbed the data to the development server?


Backup/Scrubbing
--------------
     > exit;
     vagrant@prod:~$ /vagrant/backup.sh

* The backup strategy that I used was [mongodump](http://docs.mongodb.org/manual/reference/program/mongodump/).
* Ideally I would need to learn more about file system snapshots which seems a better way to push incremental backups especially when the data set gets huge (* Example >200GB*).
* The backup script calls a command line interface I built in [Java](https://github.com/ramziyassine/mongo-scrub/blob/master/src/main/java/com/hudl/db/cli/CliInterface.java).
* You don't need to use it as the bash script takes care of calling it but here is what is does.

Java CLI usage
--------------
    $ java -jar hudl-utils.jar
    usage: hudl-utils
    -b,--backup <arg>                The path of the backup bson file. The
                                     scrubbed file will be return in the same
                                     directory as the original file, with a
                                     -scrubbed added to the name of the file.
    -h,--help                        Tool usage
    -v,--version 0.4.8.15.16.23.42   Tool usage

Please visit the java code in this repository. Below section show how the scrubbing process works.

Restore
--------------
    vagrant@prod:~$ /vagrant/restore.sh

Et voilà, What happened?
* The restore script located the latest backup dump
* Used [mongorestore](http://docs.mongodb.org/manual/reference/program/mongorestore/) pointing to the *test* box's mongodb
* Let us check that the data in the dev VM is scrubbed?

Access the data of the dev VM
--------------
     $  mongo -host $FOREIGN_IP --port $FOREIGN_MONGO_PORT
We could open another terminal and ssh to the test machine, but this is nice and sweet, simply use the Mongodb client to connect to the test server

Check data is scrubbed?
--------------
    > db.users.find();
    { "_id" : ObjectId("546f1831be4007ef545c812d"), "Email" : "csiicquiezingxqzylpi@testing.com", "UserName" : "Cailin_Luna", "FirstName" : "naliCi", "PhoneNumber" : "0054XX648XXX", "LastName" : "uaLn", "Password" : "5e706cb04271f1e2def7507a22994627b71b7e5dd0aa1ed6b6956007f9fd16ff037cfe038f6fc82d4eab6a8adc51703c8a6550d4915ff2dbf659c6b990d05937" }

The above was just a sample of the data scrubbed, notice I did not scrub the user name (just to test the configuration)

I am done?
--------------
     > exit;
     $ exit
     $ vagrant destroy
If you want a clean slate and total removal of the virtual machines use vagrant destroy.

### How is scrubbing carried on

* [Please visit the documentation in the properties file](https://github.com/ramziyassine/mongo-scrub/blob/master/src/main/resources/scrub.properties).
* So when I load the [BSON](http://bsonspec.org/) files I convert them into BSonObject and iterate through its Map to convert the values based on the scrub rules in the properties file.




### Design consideration

* I am going to play some more in Mongodb's replica set, the current restore process does not guarantee a zero downtime.
* The idea would be to take out a secondary member and restore the data to him and then join the group and eventually the primary will catch on.
* I still have more research on this to do.
* As far as db growth, as I mentioned above, take snapshots instead of database dumps might workout better as the database grows.
