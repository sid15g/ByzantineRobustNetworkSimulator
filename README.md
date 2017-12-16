# Implementation of Byzantine Robustness over a Network Layer

Implemented as a academic project, under [Prof. Haibin Zhang](https://www.csee.umbc.edu/~hbzhang/) in `CMSC-626 (Principles of Computer Security)`

### About:
Aim to simulate a network with byzantine faults and overcome them using protocol defined in [Network Layer protocols with Byzantine Robustness, Radia Perlman](http://publications.csail.mit.edu/lcs/pubs/pdf/MIT-LCS-TR-429.pdf).

### Prerequisites:
* Java 8
* Maven


### How to simulate: 
* Compile the project using Maven and `pom.xml`
* Create a folder named 'bftNetwork\_lib' and copy the dependencies in this folder
* Copy the Java archive (jar) to the parent folder of 'bftNetwork\_lib'
* Use the below command to run the simulator
``` java -jar bftTolerantNetwork.jar ```


### Experiments performed:
* Manipulating network packets
* Sending invalid Identification/Faking Neighbors (a switch 'C', faking 'A' as 'B' and 'B' as 'A')
* Flooding invalid Public Key List
* Flooding invalid Link States by decreasing link costs


### Future Work (Phase 2):
* Adding more faulty behaviors and testing
* Implementing route calculation and sending Datagrams
* Adding end-hosts as network nodes to control data packets
