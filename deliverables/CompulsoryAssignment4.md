
# Compulsory Assignment 4

## Team and project

### Meetings
[Meeting minutes](https://github.com/inf112-v20/Team-Vent/blob/master/deliverables/Meetings.md)

### Roles

When starting the project we came up with several roles for the team. We have not had to change these roles through the project, and everyone is happy with how the roles were distributed. 

Team-Vent roles are:
* Teamlead: Caroline
* Customer Contact: Marius
* Responsible for testing: John Isak
* Responsible for networking: Simen 
* Responsible for the structure of the game: Oda

### Retrospective

The project and semester is coming to an end and it is time to take a look back and see how the semester went. 

Throughout the whole project we have mainly been working by the project methodology Kanban. This is a methodology that has a projectboard in focus and in the last two iterations we have really approved our work with the project board. It has helped us get a good overview of what tasks need to be done, and who is working on which task. We started the iteration breaking down our requirements into issues on the project board. On a meeting once a week approximately we looked through the project board together and saw if we were missing something, or if something needed to be moved to either in process or done. In this iteration we have been keeping up the good work with how we write issues, and there has been few misunderstandings. Working so good with the project board lately has really been an advantage for us as a team. 

It has now been almost two months with lockdown and communication over Discord, Slack and Facebook. In the beginning we used some time to get used to working remote, but the last few weeks this has worked very well. Everyone takes the project and workload serious, and we have managed to have two set meetings every week. If someone has had something they want answer to outside the meetings, everyone has been available in our facebook chat. Overall our communication has been really good throughout the project. Since the university shut down we have all have had good experiences with working remote. 

If we did start from the beginning with the project with all the knowledge we have today, there is some things we would do differently. We would probably work more in the beginning to keep the code structured and tidy. To maintain code over time is something we have learned that is really important. Another thing that we would do differently is to separate graphics from business code from the start. A few weeks into the project we refactored to use the design pattern “Model View Controller”. This would be much easier if we used it from the start. We would also use more time on testing, for example to create tools to help us write tests easier.  

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
    - Implement all cards 
    - Robot can take damage from laser 
    - Robot can take damage from another robots laser
    - Decide if a player has won 
    - The player can lose 
    - The robot can be repaired
    - Time limit on programming robot 
    - Push another robot
    - When the robot takes damage, the players cards get locked
    - Show game information in game interface
    - When the robot takes damage, the player receives fewer cards next round

This is a full list of all our requirements which we have considered as MVP. We consider these as MVPs because with these requirements it is possible to play a fully functional RoboRally. 

### This last iteration we have worked mainly with these requirements:

* Implement all cards 
* Decide if a player has won 
* A player can lose
* Time limit on programming robot 
* Show game information in game interface

Since this was the last iteration we wrote some extra user stories, so we are sure we cover all of the requirements. 

Throughout the whole process we have discussed some “Nice to have”-features that we wanted to include in our game, when all MVPs are fulfilled. These are features that we do not consider as critical for the game. One of those was priority on cards. We concluded that it is possible to play a functional game without it, so we decided to put this as “Nice to have”, and not as an MVP. Another “Nice to have” feature was music. We also decided pretty early that Powerdown was a “Nice to have” feature, but we all agreed that this would be a cool feature to include, and probably make the game even more exciting. 

### User stories

**As a player I want to be able to choose all the different cards which is included in the RoboRally rulebook**

* Tasks: 
  * Implement all the cards from the rule book
  * Create a deck of cards that has the right amount of each card type
  * Match the cards robot to behavior

* Test requirements: 
  * Start the game in developer mode. Look at the cards you have been dealt, and press ‘G’ a couple of times to force deal new cards. All cards should show.
  * When the user programs the robot with a U-turn card the robot turns around by 180 degrees. 
  * When the user programs the robot with a Move-1 card the robot takes one step forward there is not a wall in the way 
  * When the user programs the robot with a Move-1 it has the same effect as playing two Move-1-cards in a single round. (The robot takes a step forward if there is not a wall in the way, then another step if there is not a wall in the way.)
  * When the user programs the robot with a Move-1 it has the same effect as playing three Move-1-cards in a single round.
  * When the user programs the robot with a TurnRight card the robot turns to the right by 90 degrees.
  * When the user programs the robot with a TurnLeft card the robot turns to the left by 90 degrees.

**As a player I want phases to be implemented so that I can play the game**

* Tasks:
  * Implement a structure capable of handling the events of phase, and display them visually.

* Test requirements: 
  * Do not start the program in dev mode. 
  * End turn given everyone has pressed “end turn”. I want the program to go through all the parts of a phase.

**As a player I want my robot to touch flags**

* Tasks:
  * Find flags automatically when loading the map
  * Check whether the robot is standing on a flag
  * Implement rules as below 

* Test requirements:
  * When a robot is standing on a flag or repair site at the end of a phase its backup position changes to that position 
  * When a robot is standing on a flag or repair site and the end of a phase it loses one damage
  * When a robot visits a flag in the right order it is registered as visited
  * When a robot visits a flag in the right order it is not registered as visited

**As a player I want robots to be able to push each other as this is part of the roborally ruleset**

* Tasks: 
  * Implement pushing of other robots.

* Test requirements: 
  * Given: Play a move 1,2 or 3 card into a chain of one or more robots Then they should be pushed. 
  * Given: Play a move 1,2 or 3 card into a chain of one or more robots with a wall 1 or 2 tiles away blocking the path. Check that the chain of robots will be pushed and blocked by the wall when there is a wall in the path.

**As a player I want cards to become locked in their programming slots when the robot takes damage in the same manner as in the RoboRally rulebook**

* Tasks: 
  * Make cards not be cleared from programming slots and make player unable to undo the cards programming slot-placement based on the roborally rulebook.

* Test requirements: 
  * Given: My robot has taken enough damage according to the RoboRally rulebook to have one or more of my slots locked. The cards will remain in their slots at the end of a turn, and I won’t be able to undo their placement.

**As a player I want to receive fewer cards when the robot takes damage in the same manner as in the RoboRally rulebook**

* Tasks: 
  * Deal the player fewer cards based on how much dmg he has taken according to the roborally rulebook.

* Test requirements: 
  * Given that my robot has taken enough damage to have me be dealt fewer cards according to the RoboRally rulebook. Then I should be dealt fewer cards according to the roborally rulebook.

**As a player I want the robots to not be able to move through walls when playing programming cards or getting pushed**

* Tasks: 
  * Create a function which checks if a robot is about to move through a wall. If that is the case, stop the robot from moving

* Test requirements: 
  * Given a robot standing on a tile with a west facing wall and is about to play a move one 1 card facing in the west direction. The location remain the same after the phase

**As a player I want to win the game when I have obtained all the checkpoints**

* Tasks: 
  * Make a victory screen appear when the player has obtained all the checkpoints. Delete remove all other robots from map and give them a losing screen.

* Test requirements: 
  * Given that a robot has obtained all checkpoints. Check that “You Win” appears on the screen and that other robots are removed from the game.

**As a player I want to lose the game when I have lost all my lives**

*Tasks: 
  * Make a losing screen appear when the player has lost all the lives and have the robot never respawn.

* Test requirements: 
  * Given that a robot has lost all lives. Check that “You Lose” appears on the screen and that you do not respawn or that your robot interacts with the game in any way.

**As a player I want there to be a time limit on how long players get to program their robot**

*Tasks: 
  * Implement a client side timer that forces a player’s “end turn” when the time runs out.

* Test requirements: 
  * Given that a programming phase, a timer should count down from a set time. Then if the player does not press end turn and the countdown reaches 0, the game should “force end turn” and the player’s remaining programming slots should be filled with random cards from his hand.

**As a player I want to be able to play a round of roborally with other human players**

* Tasks: 
  * Make the programming phase multiplayer compatible.

* Test requirements: 
 * Given 2 players have started a game though the lobby system and both players play 5 move 1 cards. Both robots should move 5 spaces on both player's clients
