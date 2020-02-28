# Compulsory Assignment 2

## Project and project structure 

After the first assignment, we agreed that all of us are developers, but that each and one of us will also have a special responsibility and role. In the feedback after the last iteration we got a recommendation to choose one of us to be responsible for testing. We will all take responsibility for testing our code as we write it, but one person will review the tests, suggest further testing and keep an eye on test coverage. John Isak stepped up to the task. Caroline is still the team lead, and that still works fine. Marius is the customer relation manager, and has the responsibility with keeping contact with the customer. 

After further discussion we came up with several new roles for the team. We decided that Oda will be responsible for the structure of the game, so we made a role called; system architect, and assigned the role to Oda. We also discussed that we wanted to try that one person is responsible for the network, a so called network administrator, and assigned the role to Simen. We will see how this role will work out.

Our group dynamic still works fine, and we consider us a good working team. Our communication in between the meetings is still good, and everyone is quick responding on Slack and in our Facebook chat. Our communication on the meeting on other hand can improve. It is given that in a team, people are at different level when it comes to coding for example. When we do not have time pressure, it is then important that everyone is patient, and accepts that people code in different speed. We have all discussed this and wil take it in consideration in the next assignment. 

## Retrospective

We still work by the project methodology Kanban, and use our projectboard more frequently than in the last compulsory assignment. This has helped us a lot get a better overview of our project and see who is working with which task. Everyone in the team is free to make issues on the project board. Since this is our first time making issues, we have experienced that when assigning an issue to one person, we found out later that each issue requires more work than we thought. This makes it harder to know what people actually are working on in the codebase and for others to modify the code. Until next time we need to make issues for every small implementation, so there will be no insecurity and misunderstanding. This will also improve our communication in the team. 

In this assignment we also started using code review and pull request, which also works really well. Since this is the first assignment we actively started using the project board, we can also get better distributing tasks to each other. We are used to working differently, and someone likes to just make tasks for their own, but other prefer the tasks being made in plenum, and then get assigned to them. We need to take this in consideration until next assignment, so everyone in the team feels included. Each and one of us also works in their own branch, and then merge into master branch. Here we can get better at pushing to github more regularly, so we can see which classes each are working on. 

It is important that everyone in the team makes approximately the same amount of commits. We have tried our best this time, but as mentioned people our at different level when it comes to coding. In the next assignemnt we will actively try pair programming, and see if this works out well, and boost all of the team members. 

We have regular meetings, and two days per week our meetings are fixed. If we need to have meetings more frequently, we also tend to make this happen. We set an agenda for each meeting, and try our best to follow this, but it happens that we start discussing other things, and can not keep up with the agenda. It is also easy to get distracted with coding on the meetings, when there is other important things we need to discuss. This is something we will work on until next assignment. 

Earlier we spent a lot of time creating an UML in the beginning and have now experienced that this is not as effective as we wanted it to be. In the future we will use less time creating a UML, and more time discussing the big design factors. We think that it needs a lot of experience creating big projects, so we have found out that the implementations we make, changes over time when working on the project. We will plan our interfaces in the start of every assignment, but on a bit more high level, not discuss specific implementations in the beginning. We will also attach our final UML in deliverables. 

Regarding our user stories, we experienced that it is hard making clear tasks in the beginning and then work by them, since things change when the coding starts. But similarly as with the UML, we will plan our tasks on a more high level, and then update them along the way.

**Three improvements from the retrospective:**

- Make issues in the project board for small things as well as the bigger things, so there will be no misunderstandings.
- Use less time on the UML in the beginning, and more time discussing the big design factors. 
- Tasks to fulfill our user stories. 

## Meetings 

**Meeting 13.02.20**

Did not receive the second compulsory assignment yet, but we talked and discussed what we had achieved at this point in the process of creating roborally. We also agreed on when we were going to meet the next time. Everyone in the team attended the meeting. 

**Meeting 19.02.20**

**Agenda**

- Agree on the requirements and assign jobs for everyone based on those requirements
- Play RoboRally in real life
- Discuss the use of mockito
- Acceptance criteria
- Update UML 

**Meeting notes**

Started the meeting by playing roborally. Then we discussed which requirements we are going to implement in the second iteration. After that we formulated user stories and continued making our UML, we did not have time to assign everyone tasks, and decided to do so the next day. Everyone in the team attended the meeting.
	
**Meeting 20.02.20**

**Agenda**

- Update project board with tasks
- Assign everyone task(s) 
- Continue discussing what to include to achieve our user stories 

**Meeting notes**

We updated our project board with tasks we needed to do get one step closer achieving our user stories, then we assigned everyone some tasks and started working. In the end of the meeting we agreed on what each and everyone should do until the next time we meet. Everyone in the team attended the meeting. 

**Meeting 24.02.20**

**Meeting notes**

Decided to have a short meeting after the lecture to catch up on how everyone is doing with their tasks. Everyone attended the meeting. 

**Meeting 26.02.20**

**Agenda**

- Discuss project methodology and group dynamic - how is everything going so far 
- Discuss problems/bugs in the game 

**Meeting notes**

- Assigned presentation workers
- Assigned work for waiting phase and cool robot implementation (rotation)
- Big map
- Decided how we are gonna look into big map 
- Everyone attended the meeting 

**Meeting 27.02.20**

**Agenda**

- Prepare presentation
- Catch up on the last things thats needs to be done before the deadline 

**Meeting notes**

- Presentation prepared and ready for screening
- Merge conflicts with waiting and phases implementation got more or less solved
- Experimented with GUI implementation
- Agreed to what we are gonna hand in for assignment 
- Bug fixes
- Everyone attended the meeting 

## Requirements 

    - Show the game board 
    - Show a robot 
    - Program robot
    - Hand out cards
    - Play a round
    - The robot can die 
    - Decide if a player has won 
    - Visit a flag
    - The robot can take damage
    - The robot can die 
    - The player can die 
    - The robot can be repaired
    - Time limit on programming robot
    - Walls stops robots
    - Walls stops lasers 
    - Robot stops lasers 
    - Implement a phase
    - Push another robot 

## Second iteration requirements:
- Program robot 
- Hand out cards
- Play a round 
- The robot can die 

## User stories 

As a player I want to be able to select 5 cards from the hand i’ve been dealt so that I can program my robot. 
- Given that cards have been dealt from which to choose, and that the player has chosen to place card A in slot B, card A is placed in slot B.  

As a player I want my robot to move the way i programmed it to so I can see my move being performed. 

As player I want my robot to die when it goes outside the board.




