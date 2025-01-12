Latest build status: 
![last integration](https://github.com/Gho-Ost/scenario-quality-checker/actions/workflows/integration.yml/badge.svg)

# Scenario Quality Checker (SQC)

For analysts documenting functional requirements with scenarios, our SQC application will provide quantitative information and enable detection of problems in functional requirements written in the form of scenarios. The application will be available via GUI and also as a remote API, thanks to which it can be integrated with existing tools.

---
<b>Important:</b><br>

The project was done in collaboration with other developers and constitutes the final project for Software Engineering classes. It was forked here to allow for easy viewing of the developer's work.

<b>Link to original repository where repo was held:</b> <br>
[Original Repository](https://github.com/Gho-Ost/scenario-quality-checker) <br>

<b>Links to collaborators:</b> <br>
[Gho-Ost](https://github.com/Gho-Ost) <br>
[adammielniczuk](https://github.com/adammielniczuk) <br>
[krzysztof-m-w](https://github.com/krzysztof-m-w) <br>

---
Backlog sheet access: [backlog](https://docs.google.com/spreadsheets/d/10xPEoCOPM9XNCSuVxjXvBpm4q6dU80llhul9FbcC-B8/edit?usp=sharing)<br>
Definition of Done sheet: [DoD](https://docs.google.com/spreadsheets/d/1tAZz23FwqmvO13xXP5R6w4fjLJbzPUflcsjcx2UOEy8/edit?usp=sharing)

---

## Format of scenarios:
- The scenario includes a header specifying its title and actors (external and system)
- The scenario consists of steps (each step contains text)
- Steps can contain sub-scenarios (any level of nesting)
- The steps may start with the keywords IF, ELSE, FOR EACH

### Example:<br>
Title: Book addition<br>
Actors:  Librarian<br>
System actor: System<br>

- Librarian selects options to add a new book item
- A form is displayed.
- Librarian provides the details of the book.
- IF: Librarian wishes to add copies of the book
    - Librarian chooses to define instances
    - System presents defined instances
    - FOR EACH: instance:
        - Librarian chooses to add an instance
        - System prompts to enter the instance details
        - Librarian enters the instance details and confirms them.
        - System informs about the correct addition of an instance and presents the updated list of instances.
- Librarian confirms book addition.
- System informs about the correct addition of the book.

### Corresponding JSON input format:<br>
```json
{
	"title": "Book addition",
	"actors": ["Librarian"],
	"systemActor": "System",
	"steps": [
		{"Librarian": "selects options to add a new book item"},
		"A form is displayed",
		{"Librarian": "provides the details of the book."},
		{"IF": {"Librarian": "wishes to add copies of the book"}},
		[
			{"Librarian": "chooses to define instances"},
			{"System": "presents defined instances"},
			{"FOR EACH": "instance:"},
			[
				{"Librarian": "chooses to add an instance"},
				{"System": "prompts to enter the instance details"},
				{"Librarian": "enters the instance details and confirms them."},
				{"System": "informs about the correct addition of an instance and presents the updated list of instances."}
			]
		],
		{"Librarian": "confirms book addition."},
		{"System": "informs about the correct addition of the book."}
	]
}
```

---

## Working with REST api

### working with JSON input in request body

Step counter:
- GET /scenario/stepcount

Actor step counter:
- GET /scenario/actorstepcount/{actor}<br>
*actor - string name of the actor*

Keyword counter:
- GET /scenario/keywordcount

Level cutting:
- GET /scenario/levelcut/{maxLevel} <br>
*maxLevel - integer value of cutting depth*

Missing actor:
- GET /scenario/missingactor

Download text:
- GET /scenario/download

Rename actor:
- GET /scenario/renameactor/{oldactor}/{newactor} <br>
*oldactor, newactor - string names of actor to be replaced, new actor name*

### working with stored scenarios
*title - title of the scenario*

Adding a scenario to storage:
- POST /scenario <br>
*with included request body in JSON*

Appending a step to a scenario:
- POST /scenarios/{title}/step

Getting all scenarios:
- GET /scenarios

Getting a scenario by title:
- GET /scenarios/{title}

Deleting all scenarios:
- DELETE /scenarios

Deleting a scenario by title:
- DELETE /scenarios/{title}

Deleting a step of a scenario:
- DELETE /scenarios/{title}/step/{stepLevel}<br>
*stepLevel - string step level of the deleted step e.g. 3, 2.1.2*

Step counter:
- GET /scenarios/{title}/stepcount

Actor step counter:
- GET /scenarios/{title}/actorstepcount/{actor}<br>
*actor - string name of the actor*

Keyword counter:
- GET /scenarios/{title}/keywordcount

Level cutting:
- GET /scenarios/{title}/levelcut/{maxLevel} <br>
*maxLevel - integer value of cutting depth*

Missing actor:
- GET /scenarios/{title}/missingactor

Download:
- GET /scenarios/{title}/download

Rename actor:
- GET /scenarios/{title}/renameactor/{oldactor}/{newactor} <br>
*oldactor, newactor - string names of actor to be replaced, new actor name*

---

## GUI
![image](https://github.com/MatTheTab/scenario-quality-checker/blob/main/GUI_images/Screenshot%202023-07-05%20122952.png)<br>
![image](https://github.com/MatTheTab/scenario-quality-checker/blob/main/GUI_images/Screenshot%202023-07-05%20123101.png)
