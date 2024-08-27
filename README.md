# Floors – Copy of Doors

## Information
A game based off Roblox: Doors, created in Java Swing

## Created by:
* Rishi Aluru
* Nora Schellhammer
* Sunny Vuyyuri

## Documentation
### Game.java
Game()
* Constructor
* Creates a new Windows 
* Creates a new Handler class
* Adds the Player object

start()
* Creates a thread
* Starts the thread and sets running to true

stop()
* Continues to try and joins the thread, and sets running to false
* If an error occurs, it prints out the error

run()
* Basic FPS and tick speed things(Used in basically every Java game)

tick()
* Calls the Handler's tick function

render()
* Buffers the FPS
* If buffer is null, it creates a new buffer
* Renders graphics

### GameObject.java
GameObject()
* Constructor
* Sets the x, y, and ID to the parameters

tick()
* Abstract method
* Used to set the tick speed

render()
* Abstract method
* Used to render the object

### Handler.java
tick()
* Gets all of the objects in the game and calls the tick method of said object

render()
* Gets and renders all of the objects into the game

addObject()
* Adds an object into the game

removeObject()
* Removes an object from the game

### ID.java
* It is an Enum
* Stores the IDs of objects

### Window.java
* Creates the JFrame Window where the game is going to be played.
