# Floors

## Information
A game based off Roblox: Doors, created in Java Swing

## Created by:
* Rishi Aluru
* Nora Schellhammer
* Sunny Vuyyuri

## Documentation
### Game.java
Game()
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
* Sets the x, y, and ID to the parameters

tick()
* Abstract class
* Used to set the tick speed

render()
* Abstract class
* Used to render the object


### Handler.java

### ID.java

### Window.java

### Player.java

### Window.java
