
# Compulsory Assignment 3

## Meetings

**Meeting 04.03.2020**

Agenda:
- Requirements for third iteration 
- User stories - tasks 
- Phase implementation talk
- Something we want to ask Siv? 
- Other questions? 

Meeting notes: 

Discussed more MVP requirements. Agreed on requirements for the third iteration. Talked about different implementation solutions. 

Attendance: Everyone

**Meeting 06.03.2020**

Agenda:
- Network
- Sketch user interface
- Discuss more solutions 
- Pairprogrammering

Meeting notes:

Made a sketch of the user interface for improvements. Also discussed some more networking and multithreading. Did not get time for pair programming.

Attendance: Everyone

**Meeting 11.03.2020**

Agenda:
- Programming phase and server interaction 
- Bug report 
- User stories 

Meeting notes:
Discussed how the server interaction would work. Discussed some bugs which needed fix. Changed a bit on the user stories. 

Attendance: Everyone 

**Meeting 12.03.2020**

Agenda:
- Come up with good solutions to handle the Corona situations
- User stories

Meeting Notes:

We will use discord for voice chat, codeshare for sharing our code, awwapp for a whiteboard, and slack for chat log with links and other useful things. Talked even more about the our user stories. (This was the day everyone had to leave campus) 

Attendance: Everyone except Caroline 

**Meeting 17.03.2020**

Meeting notes:

Short meeting, so had no agenda. Discussed how to solve laser/take damage implementation 

Attendance: Everyone

**Meeting 19.03.2020**

Agenda:
- Go through feedback from last compulsory assignment 

Meeting notes: 
We looked at the feedback, and asked Eric some questions about it. Everyone also had a lot to do in other courses, so we agreed on a meeting over the weekend, and to continue our work then. 

Attendance: Everyone 

**Meeting 23.04.2020**

Agenda: 
- Catch up on where everyone is at and talk about what tasks we have left to achieve our user stories. 
- Update user stories with acceptance criteria 
- Allocate tasks
- Other questions? 

Meeting notes:
We discussed some of the tasks we have left to complete our user stories, we then tried to make a priority list with things that needs to be done before the deadline. 

Attendance: Everyone 

**Meeting 26.03.2020**

Agenda
- Retrospective
- UML
- What has to be done before deadline tomorrow

Meeting notes:
Simen did a demo of the new lobby screen. Discussed some points for the retrospective and allocated tasks before the deadline. 

Attendance: Everyone 

## Team and project

Since the last assignment times have changed drastically. In comparison with other things the crisis has also affected our project work. The last two weeks we have had our meetings over discord and to be honest it has not been as effective as we hoped. 
 
The roles we came up with in the last assignment still stands. Caroline is still the team lead, Marius is the customer relation, John Isak is responsible for the testing, Simen is responsible for networking and Oda for the structure of the game. Even if we do not have physical meetings, we have not changed the roles in the team. Before the last assignment we came up with the role network administrator and assigned the role to Simen. In the last three weeks Simen has really taken his role seriously and done a great job as network administrator.
 
**Retrospective**
 
We still go by the project methodology Kanban and use to update our project board frequently with tasks that need to be done. This helps us get an overview of the tasks we need to complete and who in the team works with which task. Everyone in the team is free to make issues on the project board and assign it to either him/herself or others. Since last time we have improved the way we write issues and try to make an issue for every small implementation. Until next time can we get even better updating the project board. Everyone is working on their own branch, and when an implementation or functionality is done, the person or persons make the branch ready for code review and makes a pull request. When everyone is happy with the functionality the branch gets merged into the master branch. 
 
Because of the crisis and the ability to meet in person we did not have a lot of time for pair programming as we hoped. Marius and Caroline managed to get some done before we had to leave campus, but we are trying our best in these times to help each other with coding and the quality of the code. 

Since the last assignment we have improved our meeting minutes. We make an agenda before every meeting and try our best to follow it. In this iteration this has worked better than the last iteration. Since the campus closed and we do not have physical meetings now, we tend to have less meetings than we had before this happened. This will be taken into consideration until the next assignment. 

**Three improvements from the retrospective:**
- Get even better updating the project board
- Routines and teamwork - try to get back to how we worked before the crisis
- More meetings

**Communication and group dynamic**
 
Our communication has also changed over the past weeks. This has also had an impact on our group dynamic. We communicate over slack, our facebook chat and on discord when we have group meetings. All in the group follow up when we have meetings over discord, but since the last iteration the motivation has not been on top and working with the project takes more time than before. The knowledge is best shared when we have physical meetings, so our communication quality has decreased over the past weeks. This is something we will work on in the future, it has just taken some time to get used to our new lifestyle back home and/or in quarantine.

## Requirements 

    - Show the game board 
    - Show a robot 
    - Program robot
    - Hand out cards
    - Play a round
    - The robot can die 
    - Walls stops robots
    - Implement a phase
    - The robot can take damage
    - Play local network multiplayer 
    - Implement lasers 
    - Implement all cards 
    - Robot can take damage from laser 
    - Robot can take damage from another robots laser 
    - Visit a flag 
    - Decide if a player has won 
    - The player can lose 
    - The robot can be repaired
    - Time limit on programming robot
    - Push another robot 
    - When the robot takes damage, the players cards get locked 
    - Deck of cards - 10 different cards (priority) 
    - Show game information in game interface

**Third iteration requirements:**
- The robot can take damage 
- Play local network multiplayer 
- Implement lasers 

**MVP requirements:**

The list above shows which requirements we consider as MVP requirements. Before every iteration we decide which requirements we will focus on and hopefully manage to fulfill. Last iteration one of the requirements we chose to focus on was “Play a round”. In this iteration we realized that part of playing a round is capturing flags. We did not implement this in the last iteration, but managed to do this in this iteration. The other requirements focused on in this iteration is: The robot can take damage, play local network multiplayer and implement lasers. We choose these requirements as MVP because these requirements are an important part of creating a functional RoboRally. 

**User stories:**
 
 1. As a user I want it to be lasers in the game such that my robot can take damage.
	
- Test requirements:
  - Given a robot in front of another robot or infront of wall laser, the robot takes 1 dmg
  
- Tasks: 
  - Create a method that checks if someone will be shot by a laser
  - Create method that finds the wall lasers

2. As a player I want to join or host a lobby where I can set up a game of RoboRally

- Test requirements:
  - Given one player is host, up to 7 other player are able to join the host's lobby

- Tasks:
  - Create a server which can handle communications between clients.
  - Create a lobby screen which initializes the server and allows players to connect.
  
3. As a player I want my robot to capture flags, so that I capture all the flags and win. 


**General tasks to achieve our user stories:**
- Do research on UI
- Refactoring

**Bugs:**
- Trying to join a game with an ip that is not hosting causes an exception
- Joining a full lobby causes the application to freeze until a slot is open or an exception is thrown
- Joining a game in progress sends the player to the lobby instead
