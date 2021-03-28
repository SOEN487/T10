# T10 - SOEN 487

This is the template provided to start the web server for Tutorial 10 which is based on Tutorial 08 .
You should download this to follow along when watching the tutorial.

The finished example can be found on the finished branch of this repository.

We encourage you to use either IntelliJ IDE **or** maven from command line in order to get the best support from our TAs.

## Included Projects ##

1. The server/REST API
   * Located in /server
   * Is used to hold the REST API built in tutorial 3

2. The example and github client which we will not be using for this tutorial

3. User, UserRest and MyResponse classes which we will use for the token authentication.

## IntelliJ Instructions ##

* For each included project, navigate to the directory and find the pom.xml file, right

click and click on "Add as Maven Project" to load up the project and start working on it.

* Browse to desired class to run the main method
* Right click associated class and choose Run main()

![Screenshot](img/fig1.png)

## Maven Instructions ##

    Run the following commands in the associated project directory:

    Compile:
        mvn install

    Run (Server)
        mvn exec:java -Pserver

    Clean:
        mvn clean


