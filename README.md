# Implementation of Byzantine Robustness over a Network Layer

Implemented as a part of academic project, under `Prof. Haibin Zhang in CMSC-626 (Principles of Computer Security)`

This project aims to simulate a network with byzantine faults and overcome them using protocol defined in `Network Layer protocols with Byzantine Robustness, Radia Perlman`

### Prerequisites:
* Java 8
* Maven

### How to simulate: 
Use Maven and pom.xml to build the project. Make sure you add the libraries defined in POM to folder named 'bftNetwork\_lib', then run:

``` java -jar bftTolerantNetwork.jar ```


### Experiments performed:
* Using Invalid keys
* Manupulating network packets
* Faking Neighbors
* Flooding invalid Public Key List

