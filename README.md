WebChicken project
---
Current project state: https://github.com/val100/WebchickServer 
Several problems were found from the first glance, estimates are given as if a professional in that particular area was doing corrections. 

Using Ant and IDE-specific configuration, medium priority, estimate = 1day
*  Does not have any standardization, every new developer will need to learn project structure and build process from the beginning
*  Complex build scripts in the future
*  No clear separation of sources and resources
*	Much more overhead of disk space, especially when using SCMs like Git, SVN
*	No ability to use other IDE than NB, thus decreasing the performance of other developers

Can be changed to Maven or Gradle which standardize building mechanisms and can manage project dependencies.

Not using of DI Containers like Spring/Guice, high priority, estimate = 2days
*	Complicates almost every class of the system. Increases time for refactoring and possibility to introduce bugs
*	Makes writing automated tests very problematic increasing time on maintenance 

No Transaction Management, critical priority, estimate = 2days
*	Because of autocommits, can lead to severe bugs

Using synchronization incorrectly, high priority, estimates = no time needed if previous items are corrected
*	There are places where synchronization is used incorrectly leading to possible bugs in concurrent environment. 

Own Implementation of Connection Pool, high priority, estimates = 0.5days
-	Much lower performance than in standard implementations (C3P0, DBCP)
-	Much poorer functionality than standard implementations, e.g. not possible to configure idleConnectionTestPeriod which would result in exceptions if no one was using connection for 8 hours (default wait_timeout in mysql)
-	Higher risks of getting into troubles e.g. Standard implementations have implemented means of checking the connections and getting out of blocked state.
-	New people in the project should learn this home-grown solution

Creating new threads on Servlets level (priority medium, estimate unknown)
-	Because threads are created each time instead of using standard thread pools, they are decreasing performance

Using scriplets in JSP (priority medium, estimate unknown)
-	Makes logic scattered across the whole project
-	Makes logic untestable via unit tests

####List of changes necessary for the expansion and development of the project 

•  https://github.com/val100/Webchick  web based application 
  1.	Navigation design (IA +UI)
  2.	Using jQuery
    *	Pagination 
    *	Filter
    *	Sorting
  3.	Role based access control
  4.	Changes in the database table relationships, constraints in accordance with the above paragraphs
  5.	Dashboard page
  6.	Android dvelopment


•	https://github.com/val100/WebchickServer 
  1.	Thread synchronization
  2.	Creating request and parsing response module
  3.	Connection Pool 
  4.	Compatibility with operation system 
