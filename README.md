   ![TeamVent_logo](assets/TeamVent_logo.png)

# RoboRally :robot:
[![Build Status](https://travis-ci.com/inf112-v20/Team-Vent.svg?branch=master)](https://travis-ci.com/inf112-v20/Team-Vent)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/17d10a236ebc4b36a52a6bcb7ca5c838)](https://www.codacy.com/gh/inf112-v20/Team-Vent?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=inf112-v20/Team-Vent&amp;utm_campaign=Badge_Grade)

This is a student project where the goal is to make a digital version of the boardgame RoboRally using libGDX and Tiled. 

## How to run the program

The program is built using Maven. To run the game, run `Launcher.java` (main) in the master branch. 

## How to play
Here is the rulebook for the boardgame:

http://www.boardgamecapital.com/game_rules/robo-rally.pdf

- When the Menu Screen comes up you have some options:

To play **Singleplayer**

-  Select the map you want to play in the dropdown list

-  Press the "Singleplayer" button
  
To play **Multiplayer:** 

**LAN:**

-  Decide who will be hosting

-  The host needs check the host checkbox and type in their IPv4 Adress in the textbox and press the "Multiplayer" button

-  The other players can then join the lobby by typing in the host's IPv4 Adress and pressing the "Multiplayer" button

-  The host selects what map to play in the lobby using the dropdown list

-  When all the players are in the lobby, the host can press the "Start Game" button

(For testing purposes, 127.0.0.1 can be used to test the mutiplayer by running the program in parallel with one host and up to 7 other players)

**Online Multiplayer**

- Install Hamachi

- Create a VPN tunnel in Hamachi. (Network>Create a new network...)

- Other players needs to join your Hamachi network. (Network>Join an existing network...)

- Same step as above but using your Hamachi network ip

# How to do manual testing

Go to Constants.java to enable developer mode. When DEVELOPER_MODE is true:

- There is no time limit for programming, and empty slots will not be filled with random cards. This means that
robots will not move unless you program them.
- You can press G to get deal new cards until you get the cards you want  

In single player mode with developer mode enabled you can also:
- Click on a robot to act as the player who controls that robot
- Click on any robot and then move it with the arrow keys. This makes it easy to place robots in any situation you want 
to test, for example move it over to a flag and then play from there, set up two robots to push each other and so on. 
- This means you can lay down cards multiple robots in the same round.

In Constants.java you can also change the time limit, or disable it entirely. 

## Known bugs
- Joining a full lobby causes the application to freeze until a slot is open or an exception is thrown

- Joining a game in progress sends the user to the lobby

- Currently throws "WARNING: An illegal reflective access operation has occurred", 
when the java version used is >8. This has no effect on function or performance, and is just a warning.

## Resources
The tiles are provided by [yeoldewebsiteknight](http://www.yeoldewebsiteknight.co.uk/roborally​).  
Skin resources by [***Raymond "Raeleus" Buckley***](http://www.badlogicgames.com/forum/viewtopic.php?f=22&t=21568) [CC BY 4.0](http://creativecommons.org/licenses/by/4.0/)

Music: 
Hustle Kevin MacLeod
Licensed under Creative Commons: By Attribution 3.0 License
http://creativecommons.org/licenses/by/3.0/
