# Real-time conditions data distribution for the Online data processing of the ALICE experiment


![alt text](https://github.com/dosarudaniel/ReliableMulticastForALICE/blob/master/ReliableMulticast.png)

This repository contains a pair of Java programs that send and respectively receive multicast messages. The messages are Java objects of the class Blob.
Blob class contain a random length, random content String (payload) and a separate field for a checksum of this String.

The sender generates and send new objects at fixed time intervals (10s) and at the same time print the current time and message content on the screen.

The receiver instances also print on the screen the current time and the received message.


Requirements:
 - javac (version 10.0.2, for Ubuntu 18.04 run `sudo apt install default-jdk`)  
 - java (version 10.0.2, for Ubuntu 18.04 run `sudo apt install default-jre`)  

Compilation:  
 `make`

Documentation:  
  `make doc`  

Running:  
`make runReceiver` # creates a Receiver process that receives multicast messages from 230.0.0.0 on port 5000   
`make runSender`   # creates a Sender process that sends multicast messages to 230.0.0.0 using port 5000

Running using bash scripts:
1. Compile using `make`
2. Change directory `cd bin/`
3. Run on the sender machine `./runSender.sh <MULTICAST_IP_ADDRESS> <PORT_NUMBER>`  
   Run on the receiver machine `./runReceiver.sh <MULTICAST_IP_ADDRESS> <PORT_NUMBER>`
