A webapp for tracking, adding, deleting, updating tasks.
Springboot framework and MySql DB is used for the development of this app. Views are created using JSP files.

My git repo: https://github.com/singhmanisha0914/task-tracker-webapplication.git
Reference project - in28minutes webapp project: https://github.com/in28minutes/spring-boot-master-class/blob/master/02.Spring-Boot-Web-Application-V2/99-step-by-step-changes.md

Create a spring project using spring Initializer: https://start.spring.io/
While creating project add following dependencies: Spring Web, Spring Boot DevTools (This prevents restarting the server
every time we change something in the configuration or code.)

Generate the project and download the zip file. Extract it in the local system and Import this project in Eclipse.

New project -> Existing maven project : Import the project

#########################################################################

URL: http://localhost:8080/

########################################################################
Spring MVC makes use of the view resolver. It will read the prefix and suffix from properties file and add it to the jsp file name.
Controller upon receiving a request will return a view to the client. One of the most powerful views technology is JSP (Java Server Pages)
Controller will redirect/return to a JSP page. All the .jsp files are placed at: /todo-webapp/src/main/resources/META-INF/resources/WEB-INF/jsp/

The jsp file inside the jsp folder is called view. 

Add following jasper dependency to enable JSP 

<dependency>
	<groupId>org.apache.tomcat.embed</groupId>
	<artifactId>tomcat-embed-jasper</artifactId>
	<scope>provided</scope>
</dependency>

Whenever we get a request for a view: Search with following prefix and suffix. Define the prefix and suffix for the views in application.properties
spring.mvc.view.prefix=/WEB-INF/jsp/
spring.mvc.view.suffix=.jsp
logging.level.org.springframework=debug

##############################################################################

Implement login
*********************
http://localhost:8080/login -> LoginController -> login.jsp

Open http://localhost:8080/login and click Inspect. Go to network tab -> Doc

Get a detailed view of transactions.

##############################################################################
			HttpRequest
Browser -----------------------------> Server (Spring Boot Web Application)
		<----------------------------
			HttpResponse

###############################################################################

Implemet login with Request Param
**********************************
Let's use query parameters and pass some variables in the login URL
http://localhost:8080/login?name=Manisha

To pass this variable to the Controller in the goToLoginPage() method, we will use @RequestParam
Now we want to show this value in the View (JSP pages) 

*******************************************************************************
To pass any data from Controller to the View is done by putting it in a Model - ModelMap
*****************************************************************************
Once the data is put in the model, the View will automatically pick it from there using the key.

########################################################################################

Logging our projects logs:
We can configure the log levels using application.properties file.

For development, we use debug level because it has more information but it has performance impact as it is printing lots
of details.
In production, we might want to use "info". Mostly, it's info.

logging.level.org.springframework=info
here, org.springframework is a package and any class inside this package will have logging level at debug.

logging.level.com.manisha.springboot.webapp=debug
We can specify our project package and set the logging level for all the class inside this to debug

Lets add logger to our classes.
Step1: Add logging.level.com.manisha.springboot.webapp=debug to application.properties file.
Step2: Create logger object in controller class
		We are using slf4j logger
Step3: Use this logger to log some information
		e.g logger.debug("Request param is: {}", name);
		For production code, we have to remove the debug instead put logger.info if the message is necessary and need to be printed.

Spring boot makes logging easy - spring-boot-starter-logging 
Transitive dependency for spring-boot-starter-web. When we have spring-boot-starter-web dependency spring-boot-starter-logging is automatically
included.

Default logging framework used by spring is: Logback with SLF4j

##############################################################################################

On the server, we have a web application running, wich is built using spring boot. Inside spring boot, we have spring MVC.
-Model: data to generate the view
-View: Show information to user
-Controller: Controls the flow

###############################################################################################
We will implement the full login page where we will take user credentials and show a welcome page.
When building enterprise solution, we use spring security to validate the data (e.g login credentials)

sending data in url is unsafe as the data over internet passes through multiple routers. 
When are making any client request using GET METHOD then the form data is sent in the URL which compromises our data as it visible on network
Therefore change the form request method to POST in the login.jsp.
Now the name and password will be sent as part of the Payload and not in the URL/Header

Change the controller definition from: 
	@RequestMapping("/login")
	public String goToLoginPage(@RequestParam String name, ModelMap model) {
		//logger.debug("Request param is: {}", name);
		model.put("name", name);
		//now put this name value in the login.jsp
		return "login";
	}

above definition to the following definition
@RequestMapping("/login")
	public String goToLoginPage() {
		return "login";
	}
	
###############################################################################################

Let's authenticate the user credentials:
Upon hitting submit button for the login page, redirect to the welcome page

create welcome.jsp file

Now when a user hits: http://localhost:8080/login
GET method is called and login.jsp page is displayed

Now when user enters the credentials and hits submit button, POST method should be called. For this we want to return
welcome.jsp page.

Earlier we captured URL/request parameter using goToLoginPage(@RequestParam String name, ModelMap model)

Now we want to capture the form parameters (credentials submitted in the form): using @RequestParam

###################################################################################################

Validate the credentials:
Create a java class AuthenticationService
#####################################################################################################

Lets create Activity Tracker class. We will add objects of ActivityTracker class in DB (H2/MySQL)

#####################################################################################################
Request vs Model vs Session
***************************
#####################################################################################################

Until now we have been using expression language to display our dynamic response data (e.g ${name}) in .jsp files.
and HTML tags for show static content. E.g "Welcome to our page"

Now we want to display list of tasks in a table and it is dynamic in nature. Just expression language is not sufficient to do that. So
we will  be using JSTL tags.  JSTL (JavaServer Pages Standard Tag Library) is a collection of custom JSP tags that provide common functionality for web development.
Therefore, add two dependencies in the pom.xml for Spring Boot 3.2.x and greater:
1. JSTL API dependency 
<dependency>
	<groupId>jakarta.servlet.jsp.jstl</groupId>
	<artifactId>jakarta.servlet.jsp.jstl-api</artifactId>
</dependency>

2. JSTL implementation dependency
<dependency>
	<groupId>org.glassfish.web</groupId>
	<artifactId>jakarta.servlet.jsp.jstl</artifactId>
</dependency>

****************************************************************
Now before we can make use jstl tags, we need to import it in the file where we intend to use it.
We want to use for loop and loop around the tasks and that is present in the JSTL core lib. There are alot of other tags that
we can use: (e.g if, forEach, import, param, redirect etc)

Add this line to listTasks.jsp:
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

we are saying that the name which we are going to use is "c" and the above mentioned uri.

##########################################################################################################

Adding Bootstrap CSS (Cascading Style Sheets) framework to Spring Boot Project using webjars:
************************************************************************
We will use bootstrap css to format our pages
 Earlier to use this, we had to manually download the jar and put it in the static (/todo-webapp/src/main/resources/static) folder. 
And every time it changed, we had to manually update the jar by reuploading the new jar. But now it will automatically get updated using the webjars.

Add new dependency in pom.xml with the version and it will automatically get downloaded from the web. We can notice that for other dependencies we did not specify the version,
maven automatically mentions the version for them. We can verify their version in effective POM tab of pom.xml.


Add following dependencies in pom.xml:
1. bootstrap:

<dependency>
	<groupId>org.webjars</groupId>
	<artifactId>bootstrap</artifactId>
	<version>5.1.3</version>
</dependency>

2. JQUERY dependency
<dependency>
	<groupId>org.webjars</groupId>
	<artifactId>jquery</artifactId>
	<version>3.6.0</version>
</dependency>

Now restart the server and go to Maven Dependencies -> 

Step1. bootstrap-5.1.3.jar and copy-> /META-INF/resources/webjars/bootstrap/5.1.3/css/bootstrap.min.css

Step2: Now go to js folder under css and copy: /META-INF/resources/webjars/bootstrap/5.1.3/js/bootstrap.min.js

Step3: Go to jquery-3.6.0.jar and copy: /META-INF/resources/webjars/jquery/3.6.0/jquery.min.js

THESE ARE THREE THINGS THAT WE WILL NEED IN JSP FILES.

webjars/bootstrap/5.1.3/css/bootstrap.min.css
webjars/bootstrap/5.1.3/js/bootstrap.min.js
webjars/jquery/3.6.0/jquery.min.js

Add css in the header of the jsp file. 
Add javascript .js file right above the closing tag of the body in the jsp file.

#################################################################################################

Formatting JSP files with bootstrap CSS framework
***************************************************

bootstrap recommends that container class should contain all the body part of the jsp file.
This will make sure all the content on the webPage is centered position rather than aligning on the left or right.

also add class table for table formatting
###################################################################################################################

Now let's add a button called "Add Task"

Go to listTasks.jsp -> href

########################################################################

Add validation to restrict null empty submissions
we can have validation on the form but they can be easily overridden therefore, it's always important to add server side validations.

4-steps to add server side validations:
1. Spring Boot starter Validation - add this dependency in pom.xml
2. Command Bean (Form Backing Object) - 2-way binding (task.jsp & TaskTrackerController.java)
3. Add Validations to Bean - TaskTracker.java
4. Display Validation Errors in the View - task.jsp

Command Bean (Form Backing Object) -> We have alot of @RequestParams and if we take each one of them separately then the code will become complex.
Rather we can directly bind these input parameters to the TaskTracker Bean.

 Using Command Beans to implement New Task Page Validations: 2-way binding (1. from tastkTracker bean of ("/add-task", method=RequestMethod.GET) to task.jsp form.
 2. From the values entered on the form of task.jsp (value="/add-task", method=RequestMethod.POST ) to the taskTracker Bean passed as parameter (i.e bind this Bean to the /add-task POST method)
  
Adding validation to the Bean:
Step1: Set minimum length validation for description in the TaskTracker.java class file
	@Size(min=10, message="Enter atleast 10 characters")
	private String description;
Step2: Add @Valid TaskTracker taskTracker in the addNewTask(ModelMap model, @Valid TaskTracker taskTracker) method defined in TaskTrackerController class
Step3: To properly display this error message "Enter atleast 10 characters" in a formatted way, we will use BindingResult in the method parameter of addNewTask((ModelMap model, @Valid TaskTracker taskTracker, BindingResult result)
Step4: To display error message, go to task.jsp
		add: <form:errors path="description"/>
		
		
################################################################################################

Add delete feature to the tasks tracker
*******************************************

Step1: First add a delete button by updating the listTasks.jsp
Add <th></th>
Add <td><a href="delete-task?id" class="btn btn-warning">DELETE ${task.id}</a></td>

Step2: Update TaskTrackerController to handle this
Create a controller method called : deleteTask(@RequestParam int id,ModelMap model)

Step3: Implement the deleteTaskById method in the TaskTrackerService class.

########################################################################################################

Add Update feature for a task 
**********************************
Step1: Add update button to the view by updating the listTasks.jsp
	<th></th>
	<td><a href="update-task?id=${task.id}" class="btn btn-success">Update</a></td>

Step2: Update TaskTrackerController to handle this
	Create a method called: showUpdateTaskPage(@RequestParam int id,ModelMap model)

Step3: Update TaskTrackerService class.
	Add findById(int id) method.
	Instead of using for loop, we are using functional programming (stream ) to find a bean with specified id.
	
Step4: Save this update to the task list
	Create a new methof in TaskTrackerController class:  updateTask(ModelMap model, @Valid TaskTracker taskTracker, BindingResult result)

Note: After updating we, see that the Target Date is lost, because we are not populating it in the form

#######################################################################################################
Adding Target Date Field to task Page
******************************************
So far, we were only allowing description to be updated for every task. Let's add target date field

To have a date pop-up coming while choosing a target date, we will use Boostrap Date Picker. It's a framework which helps us add pop-ups for date and other related things.
Add to pom.xml:
<dependency>
	<groupId>org.webjars</groupId>
	<artifactId>bootstrap-datepicker</artifactId>
	<version>1.9.0</version>
</dependency>

css- used to style the date pop-up
js - used to trigger it out

copy these paths and in task.jsp: 
css - /META-INF/resources/webjars/bootstrap-datepicker/1.9.0/css/bootstrap-datepicker.standalone.min.css
js - /META-INF/resources/webjars/bootstrap-datepicker/1.9.0/js/bootstrap-datepicker.min.js

Add this in task.jsp:
<!-- Script for poping the date while picking the target date (#targetDate where # indicates that we are using it as id). Also in datepicker framework mm indicates month-->
			<script type="text/javascript">
			$('#targetDate').datepicker({
				format: 'yyyy-mm-dd'
			});
			</script>
			
###########################################################################################################

Add a navigation path at the Top : A menu
***********************************************
Copy HTML + Bootstrap code and paste it in listTasks.jsp and task.jsp:
<nav class="navbar navbar-expand-md navbar-light bg-light mb-3 p-1">
	<a class="navbar-brand m-1" href="https://courses.in28minutes.com">in28Minutes</a>
	<div class="collapse navbar-collapse">
		<ul class="navbar-nav">
			<li class="nav-item"><a class="nav-link" href="/">Home</a></li>
			<li class="nav-item"><a class="nav-link" href="/list-todos">Todos</a></li>
		</ul>
	</div>
	<ul class="navbar-nav">
		<li class="nav-item"><a class="nav-link" href="/logout">Logout</a></li>
	</ul>	
</nav>
	
we will put all the common code inside common folder with .jspf extension (jsp fragments).
Remove the above code and put it in navigation.jspf

#########################################################################################################

Anybody can directly access http://localhost:8080/list-tasks without even logging in.
We need to fix that

Spring Security
*******************
Open pom.xml and add:
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-security</artifactId>
</dependency>

Now restart the server. we will see following log:
default username: user
default pwd: 00d2ddda-d483-49ce-820a-a8762d8d4663
Using generated security password: 00d2ddda-d483-49ce-820a-a8762d8d4663
This generated password is for development use only. Your security configuration must be updated before running your application in production.


NOW ALL OUR URLS ARE PROTECTED. FOR ACCESSING ALL THE URLs WE NEED TO LOGIN NOW.

ALSO BY DEFAULT, WE HAVE A WORKING LOGOUT URL.

########################################################################################################

Now we will configure our own spring security wherein we will have a proper user and pwd.
Create a class SpringSecurityConfiguration.java

We have defined getLoggedInUsername in both WelcomeController and TaskTrackerController because we want to capture the username even when
the user directly open /list-tasks then session data won't be store that we are storing from the welcome page.
###########################################################################################################
 Adding Spring Boot Starter Data JPA and Getting H2 database ready

/pom.xml Modified
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<dependency>
	<groupId>com.h2database</groupId>
	<artifactId>h2</artifactId>
	<scope>runtime</scope>
</dependency>

Restart the server:
URL: http://localhost:8080/h2-console
Manisha/dummydummy

Configuring Spring Security to Get H2-console Working
/src/main/java/com/in28minutes/springboot/myfirstwebapp/security/SpringSecurityConfiguration.java Modified

BY Default there are two things that are configured by Spring Security:
1. All URLs are protected
2. A login form is shown for unauthorized requests.

BY Default enabled by Spring Security:
1. Cross Site Request Feature (CSRF) needs to be disabled to access H2-Console
4. Also disable frames

SecurityFilterChain: Defines a filter chain matched against every request. By default it has following two features:
1. All URLs are protected
2. A login form is shown for unauthorized requests.

We will reconfigure it to use H2-Console
Create filterChain() method.

When we override SecurityFilterChain, we need to define entire chain again.

#######################################################################################

Making Todo an Entity and Population TaskTracker Data into H2

/src/main/java/com/in28minutes/springboot/myfirstwebapp/todo/TaskTracker.java Modified

Add to data.sql:
insert into TASK_TRACKER (ID, USERNAME, DESCRIPTION, TARGET_DATE, DONE)
values(10001,'Durga', 'Get AWS Certified', CURRENT_DATE(), false);

insert into TASK_TRACKER (ID, USERNAME, DESCRIPTION, TARGET_DATE, DONE)
values(10002,'Ambe', 'Get Azure Certified', CURRENT_DATE(), false);

insert into TASK_TRACKER (ID, USERNAME, DESCRIPTION, TARGET_DATE, DONE)
values(10003,'Sharda', 'Get GCP Certified', CURRENT_DATE(), false);

insert into TASK_TRACKER (ID, USERNAME, DESCRIPTION, TARGET_DATE, DONE)
values(10004,'Saraswati', 'Learn DevOps', CURRENT_DATE(), false);

By default these sql queries will be executed before table is created. Therefore, add following to application.properties

In Database always use single quotes.

##################################################################################################
				DOCKER INSTALLATION AND COMMANDS
				***********************************
Now we will use MySQL. we will be launching mysql as a docker container.
And we will be connecting from our application to the MYsql DB.

Step1: Installing docker:
Go to https://docs.docker.com/engine/install/
next go to https://docs.docker.com/desktop/install/windows-install/

Download - Docker Desktop for Windows - x86_64

Double click the installer and restart the system. Then click the docker to open.

Register/sign in to docker using credentials.

Now open windows powershell/mac terminal and type: #docker version
We should see the version and other information:
													PS C:\Users\kaush> docker version
													Client:
													 Version:           27.2.0
													 API version:       1.47

Now to launch MySQL using Docker, run below command:
#docker run --detach --env MYSQL_ROOT_PASSWORD=dummypassword --env MYSQL_USER=todos-user --env MYSQL_PASSWORD=dummytodos --env MYSQL_DATABASE=todos --name mysql --publish 3306:3306 mysql:8-oracle

Now to see the list of containers running:
#docker container ls

Troubleshootings that I did because I had MySql running locally on my system. Therefore the port 3306 was busy:
***************************************************************************************************************
First removed the created docker:
#docker remove mysql

Now changed the port from 3306 to 3307 and reran the command
#docker run --detach --env MYSQL_ROOT_PASSWORD=dummypassword --env MYSQL_USER=tasks-user --env MYSQL_PASSWORD=dummytodos --env MYSQL_DATABASE=task_tracker --name mysql --publish 3306:3306 mysql:8-oracle
970c7b8c4139fe7ec5a8ba20fd8b9354df7ad65d25b57e6e471440edba2b7ebc

#docker container ls
CONTAINER ID   IMAGE            COMMAND                  CREATED          STATUS          PORTS                                         NAMES
970c7b8c4139   mysql:8-oracle   "docker-entrypoint.s…"   13 seconds ago   Up 11 seconds   3306/tcp, 33060/tcp, 0.0.0.0:3307->3307/tcp   mysql


Now make following changes to the pom.xml:
Step1: Comment out the H2-dependency 
Step2: Add dependency for Mysql
<dependency>
    <groupId>com.mysql</groupId>
	<artifactId>mysql-connector-j</artifactId>
</dependency>

When connecting to H2-database, spring boot automatically creates the tables for us but for real time DBs, we need to configure following property in application.properties for spring boot to create the table for us
#spring.jpa.hibernate.ddl-auto=update

data.sql is not run for real databases.


To verify the mysql db content:
Step1: Go to mysql shell: https://dev.mysql.com/doc/mysql-shell/8.0/en/mysqlsh.html
then go to : Installing MySQL Shell -> https://dev.mysql.com/doc/mysql-shell/8.0/en/mysql-shell-install.html					
			 Installing MySQL Shell on Microsoft Windows
			 download the file and install it on the system
Step2: open terminal/powershell/mac terminal
Type:
mysqlsh

you should see MySQL JS prompt:
there type:
\connect tasks-user@localhost:3306

enter password: dummytodos

Step3: Now connect to the schema:
\use task_tracker

Step4: Switch to sql mode:
\sql

Step5: Now run sql commands
select * from task_tracker;








 