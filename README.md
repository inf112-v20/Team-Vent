   ![TeamVent_logo](assets/TeamVent_logo.png)

# RoboRally :robot:
[![Build Status](https://travis-ci.com/inf112-v20/Team-Vent.svg?branch=master)](https://travis-ci.com/inf112-v20/Team-Vent)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/17d10a236ebc4b36a52a6bcb7ca5c838)](https://www.codacy.com/gh/inf112-v20/Team-Vent?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=inf112-v20/Team-Vent&amp;utm_campaign=Badge_Grade)

This is a student project where the goal is to make a digital version of the boardgame RoboRally using libGDX and Tiled. 

## How to run the program

The program is built using Maven. To run the game, run `Launcher.java` (main) in the master branch. 

## How to play

**Note:** The game is not complete yet, but we are working on the functionality every day. 

Here is the rulebook for the boardgame:

http://www.boardgamecapital.com/game_rules/robo-rally.pdf

- When the Menu Screen comes up you have some options:

**Quick Play:** (This was our first functionality; we are now working to realize the multiplayer) 

Press Play and start the game without multiplayer.

-	Program the robot by choosing cards to the right using the keyboard numbers 1-9 (The cards appears in the number slots)

-	To generate a new hand press <kbd>G</kbd>

-	To start a round press <kbd>E</kbd>

-	You can also move around with <kbd>&#8593;</kbd> and use <kbd>&#8592;</kbd> or <kbd>&#8594;</kbd> to rotate the robot

-	The players life and HP are currently shown up in the upper left corner of the sidebar 

-	Some tiles to notice when programming the robot:
    - **Normal conveyer belt:** move 1 space in the directon of the arrow 
    - **Express conveyer belt:** move 2 spaces in the direction of the arrrow
    - **Gears:** rotate 90° in the direction of the arrows 
  
**Multiplayer:** (Playing multiplayer as of now is not completely implemented, but it is possible to set up a game using the lobby system)

-  Decide on a host

-  The host needs check the host checkbox and type in their IPv4 Adress in the textbox and press the "Multiplayer" button

-  The other players can then join the lobby by typing in the host's IPv4 Adress and pressing the "Multiplayer" button

-  When all the players are in the lobby, the host can press the "Start Game" button

-  The game itself does not currently support mutiplayer so this only starts a single player game for every player in the lobby

(For testing purposes, 127.0.0.1 can be used to test the mutiplayer by running the program in parallel with one host and up to 7 other players)

## Known bugs
- Doing inputs while a turn is in progress can lead to strange and unwanted behaviour. This happens bacuse player input is not locked while a turn is in progress.

- Trying to join a game with an IP that is not hosting causes an exception

- Joining a full lobby causes the application to freeze until a slot is open or an exception is thrown

- Joining a game in progress sends the player to the lobby instead

- Currently throws "WARNING: An illegal reflective access operation has occurred", 
when the java version used is >8. This has no effect on function or performance, and is just a warning.

## Resources
The tiles are provided by [yeoldewebsiteknight](http://www.yeoldewebsiteknight.co.uk/roborally​).  
Skin resources by [***Raymond "Raeleus" Buckley***](http://www.badlogicgames.com/forum/viewtopic.php?f=22&t=21568) [CC BY 4.0](http://creativecommons.org/licenses/by/4.0/)
