mongo-scrub
===========

### Introduction 
I am hoping to share with you what I have learned in 4-7 days. You should be able to run this on your machine 

### What is the big deal 
Following these instruction will show you how to run 2 different VMs on your machine, one will be production and the other will be a test machine. 
Both are running a mongodb server. In addition, I will show you how to backup one and push a scrubbed copy to the other machine.

### Prerequisites 

* Please [Visit the download page to install Vagrant](https://www.vagrantup.com/downloads.html) and use the appropriate download for your system
* If you are using a Windows machine please know that it does not come with a native ssh client so you will have to 
    * You will need to install [PuTTY](http://the.earth.li/~sgtatham/putty/latest/x86/putty.exe)
    * You will also need to install [PuTTYgen](http://the.earth.li/~sgtatham/putty/latest/x86/puttygen.exe)
* [Download git](http://git-scm.com/downloads)!!
* Once you have downloaded the software above please head to your **terminal**(Unix, Linux, OS X) or the **command prompt**(Windows) and:
* I recommend you run the commands below in your home directory, or directory you have read/write access to.

Downloading the code
--------------------
    $ git clone https://github.com/ramziyassine/mongo-scrub.git
    $ cd mongo-scrub/src/main/resources/vagrant/


### Guided Tour

Follow this steps

Create the VMs
--------------
    $ vagrant up
I do apologize this is my first experience with vagrant and I could have create a self box but instead I have a bootstrap provisioning and it will take a while until both VMs are up.
This is a good time for a ping pong game (maybe one set) or cup of coffee!!

### What happened

 Two virtual machines have been created for you and they are ready to roll

|Name | IP address  | Mongodb server port
|----|---------- |----------------------
|prod | 192.168.4.15| 4815
|test | 192.168.4.16| 4816

* The boxes already have java 8 installed (run the scrubber program)
* Mongodb server
* Equipped with utilities (bash script) to conduct the backup and restoration.


Let us Populate the Production database with some data
--------------
    $ vagrant ssh prod
This step on Windows will need to put done on putty. [Please check this good resource to see how](http://stackoverflow.com/questions/9885108/ssh-to-vagrant-box-in-windows)
You can still run the command above in the prompt and vagrant will tell you what to do.

Generate
--------------
    vagrant@prod:~$ /vagrant/generateData.sh
You can execute the bash script above on the production server and it will populate the database with 100 users

Take a peek
--------------
    vagrant@prod:~$ mongo --port $LOCAL_MONGO_PORT

The vagrant file calls for the help of init bash script to setup environement variables like **LOCAL_MONGO_PORT**

    > db.users.find();

* Data has been populate to our users database (Type the above in the Mongo shell)
* Let us see how we can back up/scrub the data
* Let us see how we can push the scrubbed the data to the development server


Backup/Scrubbing
--------------
     vagrant@prod:~$ /vagrant/backup.sh

* The backup strategy that I used was [mongodump](http://docs.mongodb.org/manual/reference/program/mongodump/)
* Ideally I would need to learn more about file system snapshots which seems a better way to push incremental backups especially when the data set gets huge (* Example 200GB*)
* The backup script calls a command line interface I build in Java
* You don't need to use it as the bash script takes care of it but here is what is does

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

Please visit the java code in this repository. Below I document how I conduct the scrubbing operation and how it configuration bound.

Restore
--------------
    vagrant@prod:~$ /vagrant/restore.sh

Et voilà, What happened?
* The restore script located the latest backup dump
* Used [mongorestore](http://docs.mongodb.org/manual/reference/program/mongorestore/) pointing to the *test* box's mongodb
* Let check that the data in the dev was scrubbed

Login to test
--------------
     $ vagrant ssh test
Open a new terminal and access the same mongo-scrub directory


### How is scrubbing carried on

* [Please visit the documentation in the properties file](https://github.com/ramziyassine/mongo-scrub/blob/master/src/main/resources/scrub.properties)
* So when I load the [BSON](http://bsonspec.org/) files I convert them into BSonObject and iterate through its Map to convert the values based on the scrub rules in the properties file




### Design consideration

* I am going to play some more in Mongodb's replica set, the current restore process does not guarantee a zero downtime
* The idea would be to to a secondary member and restore the data to him and then join the group and eventually the primary will catch on
* I still have more research on this to do.
* As far as db growth, as I mentioned above, take snapshots instead of database dumps might be workout better as the database grows.


