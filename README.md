# About the Project 

This project consists of clients and server which are communicating with each other using Remote Procedure Call(RPC).
This project is divided into 3 parts. Each part consist of one client and one server class.

## Part I:

Part I consist of a a multi-threaded file server that supports UPLOAD, DOWNLOAD, DELETE, and RENAME file operations. This part is implemented using Java RMI API. 

## Part II:

Part II consist of file transfer between the client and the server where client has one folder which get synchronized with the server folder. Whenever changes are made to the synchronized folder at the client side, e.g., creating a new file, updating a file, or deleting a file, the client runs a thread in the background which periodically send the updates to the server to do the synchronization.
This part is also implemented using Java RMI API. 

## Part III:

Part III implements a computation server which support add(i, j), and sort(array A) operations using synchronous, asynchronous gRPCs. In synchronous gRPC, the client makes the RPC call and waits for the result from the server. In asynchronous RPC, the client makes the RPC call and waits for an acknowledgment from the server, and continues after receiving an acknowledgment. On the other hand, after completing the computation the server sends the result of the call back to the client.
This has been implemented using gRPC.


# How to setup the project and compile it

## Instructions

### IntelliJ IDEA

1. Open IntelliJ IDEA and select _File > Open > open the directory to where the project is exported.
2. Select File > Project Structure and ensure that the Project SDK and language level are set to use Java 8.
5. In the Maven view, under lifecycle, execute maven clean and install.

### Eclipse IDE

1. Open Eclipse and select File >> Import_.
2. From the import wizard, choose Maven > Existing Maven Projects, then click Next.
4. In the maven wizard, click on Browse and select the Project Folder.
4. Click Finish to complete the import.
5. Right click on the project >> run as >> Maven clean and Maven install

# Project Structure:

1.  project >> src/main/java
	It consist of folder com.ds.part1, com.ds.part2, com.ds.part3 and com.ds.part3 for implementation of each part.
	Folder com.ds.services consist of interfaces which are used in part 1 and part 2.
2. project >> src/main/proto consists of .proto files used to implement gRPC stubs. 
3. project >> src/main/resources
	It consist of config.properties files which are used to make some variables configurable.
	
# How to run the project

## Part I:

1. Go to project >> src/main/java >> com.ds.part1 
2. Inside the folder com.ds.part1, open Server.java.
3. Run Server.java. If the message on Console is 'Server ready'.
4. Run Client.java. Run the operations from Console.

## Part II:

1. Go to project >> src/main/java >> com.ds.part2 
2. Inside the folder com.ds.part2, open Server.java.
3. Run Server.java. If the message on Console is 'Server ready'.
4. Run Client.java. Open current directory in File explorer and go to folder   'SyncClientFolder' and do any operation on the files. Folder 'SyncServerFolder' is maintained on the server side which gets in sync with 'SyncClientFolder' every 2 minute. 

## Part III: 
1. To Execute the Asynchronous calls:
   1. Go to project root >> src/main/java >> com.ds.part3.async
   2. execute class AsyncComputationServer and input 1 to start sorting server 2 to start addition server.
   3. Wait for the message "Server Started successfully!" to be displayed on the console.
   4. execute class AsyncComputationClient and input 1 to sort an array and 2 to add 2 elements.
   5. Perform the operations displayed on console.
   

2. To Execute the Synchronous calls:
	1. Go to project root >> src/main/java >> com.ds.part3.sync
	2. execute class SyncComputationServer and input 1 to start sorting server 2 to start addition server.
	3. Wait for the message "Server Started successfully!" to be displayed on the console.
	4. execute class SyncComputationClient and input 1 to sort an array and 2 to add 2 elements.
	5. Perform the operations displayed on console.


## References
* [Java RMI](https://docs.oracle.com/javase/7/docs/technotes/guides/rmi/hello/hello-world.html)
* [gRPC](https://gRPC.io/docs/languages/java/quickstart/)