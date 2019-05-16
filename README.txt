https://www.oracle.com/webfolder/technetwork/tutorials/obe/java/Maven_SE/Maven.html

https://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout.html

test app: mvn clean package

build app: mvn clean package

run client app: mvn exec:java -Dexec.mainClass="com.milos.client.App"


// multiple mains

run server: mvn exec:java@server
run client: mvn exec:java@client