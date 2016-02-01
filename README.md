# Hopefully_Unhackable - Indexing Server

## How to run:
1. Install Java 8 if not already installed
  ```
    $ sudo add-apt-repository ppa:webupd8team/java
    $ sudo apt-get update
    $ sudo apt-get install oracle-java8-installer
  ```
  Accept the terms when prompted

2. Install and run MongoDB server on default port 27017
  * [Installation Instructions](https://docs.mongodb.org/manual/installation/)

3. Get source code and switch into directory
  ```
    $ git clone https://github.com/2yangk23/Hopefully_Unhackable.git
    $ cd Hopefully_Unhackable
  ```

4. Run indexing server
  ```
    $ ./gradlew run
  ```
