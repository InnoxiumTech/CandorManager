# Candor Setup

# Loading Candor
While I currently do not have a startup script ready, you can load Candor by using the following line
`java -javaagent:<candorJar> -jar <candorJar>`

# Creating a module
Currently to create a module you will need to extends the `AbstractModule` class,
this wil give your class access to the module functionality.

You will also be required to add the following to your modules Manifest file:

`Candor-Module-Class: <path to class extending AbstractModule>`

You can view the `genericmodule` folder for an example

# Loading candor in IDE
you will need to add the follwoing to your run configuration:

`-javaagent:<explicit path to lib/jar-loader.jar>`

for Example, for me it is 

`-javaagent:"H:\Development\ModManager\libs\jar-loader.jar"`
