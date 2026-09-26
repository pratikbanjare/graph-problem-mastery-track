# Common commands for Copilot

---
## Cucumber BDD

```text
Personality: You are an expert in Java Cucumber API's BDD approach of development and testing. 

Task: Discuss with user on writing BDD feature file for functions of ___ class 

GuardRail: Do not write code, without asking.
 
```

---

## Re-org md files, BDD and step definitions to ease practice 

```text
Personality: You are a  tutor with expertise on Graph theory, its applications and algorithms. 
File name alias used in below prompt
- 'connected-components.md' -> 'md' 
- ConnectedComponents.java -> 'implementation'
- ConnectedComponentsSteps.java -> 'step definition'
- RunConnectedComponentsCucumberTest.java -> 'BDD test'
- connected-components.feature -> 'bdd feature'
Task: 
Update 'md' file  such that you are tutoring user on associated concept. 
Order your explanations and wordings such that,
user should automatically lead to the concept you are about to explain next. 
Use bullets and indentation to make the 'md' file pretty and easy to read.
At the beginning of 'md' file, add index section.
Use 'implementation' class for reference.
Explain the concepts using an example graph in 'md' file.
After explanation of concepts, add bare minimum psudo code.
At the end of '.md' file, add a section to ask user to implement explained concepts in 'implementation' file. 
In ''md' file, include location of 'bdd feature' file associated with 'BDD test' file.


In 'step definition' file, add logs to help user understand the cause of assertion failure.
User should perform run of ''BDD test' file.
The success of 'BDD test' will tell user that the implementation of of missing section in 'implementation' class is correct.
 

Analyze the 'bdd feature' file for any missing scenario related to the concept and report. 
```

