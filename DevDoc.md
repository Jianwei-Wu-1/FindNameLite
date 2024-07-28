# Development Documentation #

This is the document for the development of the plugin

# Part 1: General Design
* ParseNameExtractComponents: this is the class that will handle the test name 
* SubtractComponent_: this is the class that will handle the test body
* TestBodyPattern: this is class that contains all the test patterns and provide finder and matcher
* RuleSet: is the class that contains all implementation of the rules from the rule set


# Part 2: Design Detail
* ParseNameExtractComponents is more like a integration of different tools
and provide some information retrieval functions

* SubtractComponent_ is the implementation of parsing the test body

* TestBodyPattern will have following abilities:
  * Compare the current test case to the test pattern(model)
  * Be able to add new pattern into the pattern set
  * Provide Pattern finder 
  * Provide Pattern matcher
  
# Part 3: Supportive Graphs
All supplemental graphs are in the "DesignGraph" folder

