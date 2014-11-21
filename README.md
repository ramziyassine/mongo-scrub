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
