
# Compulsory Assignment 4

## Team and project

### Meetings
[Meeting minutes](https://github.com/inf112-v20/Team-Vent/blob/master/deliverables/Meetings.md)

### Roles

In this last iteration we continued with the same roles we have had throughout the projoect. Since we started working remote there has been a weak distinction between the different roles, but we all agreed that we were happy with how the roles have been delegated. 

Team-Vent roles are:
* Teamlead: Caroline
* Customer Contact: Marius
* Responsible for testing: John Isak
* Responsible for networking: Simen 
* Responsible for the structure of the game: Oda

### Retrospective

The project and semester is coming to an end and it is time to take a look back and see how it all went. 

**Project methodology**

Throughout the whole project we have mainly been working by the project methodology Kanban. This is a methodology that has a projectboard in focus and in the last two iterations we have really approved our work with the projectboard. It has helped us get a good overview of what tasks need to be done, and who is working on which task. We started the iteration breaking down our requirements into issues on the projectboard. On a meeting once a week approximately we looked through the projectboard together and saw if we were missing something, or if something needed to be moved to either "In Progress" or to "Done". In this iteration we have been keeping up the good work with how we write issues, and there has been few misunderstandings. Working so good with the projectboard lately has really been an advantage for us as a team. 

We tried to experience with other project methodologies like XP, especially pair-programming, but found it was hard to manage when the university shut down. We would focus even more on this if we were not working remote.

**Communication**

It has now been almost two months with lockdown and communication over Discord, Slack and Facebook. In the beginning we used some time to get used to working remote, but the last few weeks this has worked very well. Everyone takes the project and workload seriously, and we have managed to have two set meetings every week. If some of the team members needed answers outside the meetings, everyone has been available in our Facebook chat. Overall our communication has been really good throughout the project. Since the university shut down we have all had good experiences with working remote. One of the most important things we have learnt during the project is that good communication is key. Good communication helps improve the group dynamic and avoids misunderstandings. 

**What we would have done differently**

If we did start from the beginning with the project with all the knowledge we have today, there is some things we would do differently. These are:
* Work more in the beginning to keep the code structured and tidy. To maintain code over time is something we have learned that is really important. 
* Separate graphics from business code from the start. A few weeks into the project we refactored to use the design pattern “Model-view-controller”. This would be much easier if we used it from the start. 
* We would also use more time on testing, for example to create tools to help us write tests easier. 
* In the last few weeks we realized how important it has been to play the game and systematically test our features. If we had done the whole project all over again we all would use more time to play the game a couple of times each week.  

### Projectboard
[Screenshot projectboard 8th of May](https://github.com/inf112-v20/Team-Vent/blob/master/deliverables/ProjectBoard-8thMay.png)

## Requirements

    - Show the game board 
    - Show a robot 
    - Program robot
    - Hand out cards
    - Play a round 
    - The robot can die 
    - Walls stops robots
    - Implement a phase
    - Play local network multiplayer
    - Implement lasers 
    - Visit a flag
    - Robot can take damage from laser 
    - Robot can take damage from another robots laser 
    - The robot can be repaired
    - Push another robot
    - Implement all cards 
    - Decide if a player has won 
    - The player can lose 
    - Time limit on programming robot 
    - Show game information in game interface
    - When the robot takes damage, the players cards get locked
    - When the robot takes damage, the player receives fewer cards next round

This is a full list of all our requirements which we have considered as MVP. We consider these as MVP because with these requirements it is possible to play a fully functional RoboRally. 

### Fourth iteration requirements:

* Implement all cards 
* Decide if a player has won 
* A player can lose
* Time limit on programming robot 
* Show game information in game interface
* When the robot takes damage, the players cards get locked
* When the robot takes damage, the player receives fewer cards next round

Since this was the last iteration we wrote some extra user stories, so we are sure we cover all of the requirements. 

Throughout the whole process we have discussed some “Nice to have”-features that we wanted to include in our game, when all MVPs are fulfilled. These are features that we do not consider as critical for the game. One of those was priority on cards. We concluded that it is possible to play a functional game without it, so we decided to put this as “Nice to have”, and not as an MVP. Another “Nice to have” feature was music. We also decided pretty early that Powerdown was a “Nice to have” feature, but we all agreed that this would be a cool feature to include, and probably make the game even more exciting. One of the last thing we included in our game was 2 more boards to choose from. 

### User stories

**As a player I want to be able to choose all the different cards which is included in the RoboRally rulebook**

* Tasks: 
  * Implement all the cards from the rule book
  * Create a deck of cards that has the right amount of each card type
  * Match the cards robot to behavior

* Test requirements: 
  * Start the game in developer mode. Look at the cards you have been dealt, and press ‘G’ a couple of times to force deal new cards. All cards should show
  * When the user programs the robot with a U-turn card the robot turns around by 180 degrees
  * When the user programs the robot with a Move-1 card the robot takes one step forward there is not a wall in the way 
  * When the user programs the robot with a Move-1 it has the same effect as playing two Move-1-cards in a single round. (The robot takes a step forward if there is not a wall in the way, then another step if there is not a wall in the way)
  * When the user programs the robot with a Move-1 it has the same effect as playing three Move-1-cards in a single round
  * When the user programs the robot with a TurnRight card the robot turns to the right by 90 degrees
  * When the user programs the robot with a TurnLeft card the robot turns to the left by 90 degrees

**As a player I want to win the game when I have obtained all the checkpoints**

* Tasks: 
  * Make a victory screen appear when the player has obtained all the checkpoints. Delete remove all other robots from map and give them a losing screen

* Test requirements: 
  * Start the program in developer mode
  * Move the robot using the arrow keys until one tile away from the next flag
  * Play a move one card, and press end turn
  * Repeat this until you have obtained all checkpoints
  * Check that a You Win Screen appears and that all other robots are removed from the game, after the round is finished where you obtained all the checkpoints
  * Repeat steps 1,2,3 until one tile away from a checkpoint and a move 1 card played. DO NOT press end turn
  * Click a different robot and press end turn
  * Check that a You Lose Screen appears and that all robots except for the winner is removed from the game after the round is finished

**As a player I want to lose the game when I have lost all my lives**

* Tasks: 
  * Make a losing screen appear when the player has lost all the lives and have the robot never respawn

* Test requirements: 
  * Start the program in developer mode
  * Move a robot to the edge of the map
  * Play a move 1 card so the robot moves outside the map
  * Check that the robot lost a life and respawns when the round is finished
  * Repeat until you have lost all lifes
  * Check that a you lose Screen appears at the end of the round, and that you do not respawn

**As a player I want there to be a time limit on how long players get to program their robot**

* Tasks: 
  * Implement a client side timer that forces a player’s “end turn” when the time runs out

* Test requirements: 
  * Do not start the program in developer mode
  * Check that there is a timer on the end turn button
  * Leave some programming slots open
  * Check that when the timer runs out, the round begins and that any open programming slot is filled with random cards from your hand

**As a player I want phases to be implemented so that I can play the game**

* Tasks:
  * Implement a structure capable of handling the events of phase, and display them visually

* Test requirements: 
  * Do not start the program in dev mode
  * In multiplayer mode: Given everyone has pressed “end turn”. I want the program to go through all the parts of a phase
  * In single player mode: Given that I press “end turn”. I want the program to go through all the parts of a phase

**As a player I want my robot to touch flags**

* Tasks:
  * Find flags automatically when loading the map
  * Check whether the robot is standing on a flag

* Test requirements:
  * When a robot is standing on a flag or repair site at the end of a phase its backup position changes to that position 
  * When a robot is standing on a flag or repair site and the end of a phase it loses one damage
  * When a robot visits a flag in the right order it is registered as visited
  * When a robot visits a flag in the incorrect order it is not registered as visited

**As a player I want robots to be able to push each other as this is part of the roborally ruleset**

* Tasks: 
  * Implement pushing of other robots

* Test requirements: 
  * Start the program in developer mode 
  * Click robots with the mouse and then move them with the arrow keys to create an unbroken straight chain of robots
  * Move a robot so it faces the chain of robots, then play a move 1, 2, or 3 card and make sure the chain gets pushed
  * Repeat this with a wall in the chain’s path 0, 1 or 2 tiles away and confirm that pushing will be stopped by the wall

**As a player I want cards to become locked in their programming slots when the robot takes damage in the same manner as in the RoboRally rulebook** 

* Tasks: 
  * Make cards not be cleared from programming slots and make player unable to undo the cards programming slot-placement based on the roborally rulebook

* Test requirements: 
  * Do not start the program in developer mode
  * Move the robot in front of other robots or lasers by playing cards and then pressing end turn, in order to make the robot take damage
  * Check that programming slot placements don’t get cleared and that you can’t undo them when the robot has taken enough dmg (slot 5 locked with 5 dmg, slot 4 and 5 locked with 6 dmg etc…) 

**As a player I want to receive fewer cards when the robot takes damage in the same manner as in the RoboRally rulebook**

* Tasks: 
  * Deal the player fewer cards based on how much dmg he has taken according to the roborally rulebook.

* Test requirements: 
  * Do not start the program in developer mode.
  * Move the robot in front of other robots or lasers by playing cards and then pressing end turn, in order to make the robot take damage.
  * Check that amounts of cards dealt are reduced based on dmg taken, 1 less card per damage taken.

**As a player I want the robots to not be able to move through walls when playing programming cards or getting pushed**

* Tasks: 
  * Create a function which checks if a robot is about to move through a wall. If that is the case, stop the robot from moving

* Test requirements: 
  * Given a robot standing on a tile with a west facing wall and is about to play a move one 1 card facing in the west direction. The location remain the same after the phase

**As a player I want to be able to play a round of roborally with other human players**

* Tasks: 
  * Make the programming phase multiplayer compatible

* Test requirements: 
    * Given 2 players have started a game though the lobby system and both players play 5 move 1 cards. Both robots should move 5 spaces on both player's clients

## Code

[README.md](https://github.com/inf112-v20/Team-Vent/blob/master/README.md) includes how to build, test and run the program, and also instructions for how you play - both Singleplayer and Multiplayer. The file also includes known bugs.

[UML](https://github.com/inf112-v20/Team-Vent/blob/master/deliverables/finalUML.png)
