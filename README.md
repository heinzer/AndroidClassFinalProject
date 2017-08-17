# AndroidClassFinalProject
## Description 
Our project is a game where the player is given a direction and distance of a location, and the player then has to choose the correct answer from a list of four places. All of the places are located within a 8046.72 meter (5 mile) radius to the user. Once the game is completed, the player is notified if they won the game or not. The user is given the option to save the location to a list of places that can be viewed at a later time.
## Functionality:
* Uses Google’s Places API to get a list of places
* Uses the player's current location to determine places within a 8046.72 meter (5 mile) radius
* Accesses the user’s camera 
* Uses the phone’s compass sensor to determine what direction the player is pointed at in order to display the target location prompt
* Uses a database to store select locations
* Uses the phone’s accelerometer to detect when the phone is shaken. Then saves the location to the database.
## Requirements of phone: 
* Must have a Gyroscope or orientation sensor of some kind 
* Must have api version  from 18 - 23
* Works on Android 4.3 - 6.0.1
  * Must be a samsung (Nexus 5 does not work for some odd reason….)
