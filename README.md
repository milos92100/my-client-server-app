###Introduction:
This application represents a simple example of Client-Server communication.

Client:
Sending data to server and receiving an answer is done asynchronously.
There are two types of messages Account changes and User changes that should be just
an example of a domain logic or some integrated system.
Each message type has its one socket opened and the synchronisation is done in parallel.

Server:
Receiving messages and answering to them is done in a separate thread for each client.
Incoming messages are put on a queue depending on the message type.
Calculation of message duration and avg time is done by domain workers, that run in separate threads,
and poll data form their queues. Values to calculate the avg time are held in a shared state that is
synchronized.The result of the calculation is put on a separate queue, from which data is polled, formatted
and sent to console.

### Requirements:
- Maven 3.6.0 https://maven.apache.org/download.cgi

### Project setup:
Followed references:
https://www.oracle.com/webfolder/technetwork/tutorials/obe/java/Maven_SE/Maven.html

https://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout.html

### Usage:

test app: ``mvn test``

build app: ``mvn clean package``

run server: ``mvn exec:java@server``
run client: ``mvn exec:java@client``
