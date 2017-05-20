# Contacts
Welcome to contacts web application. This application is designed to help you manage your contacts. It is based on java servlet and jsp technologies without using any additional frameworks.

# Before start
Following software must be installed:
1. JDK v1.7+ 
2. Mysql v5.7+. Download https://dev.mysql.com/downloads/windows/

# Setup database
After mysql installation you need to execute database creation scripts. Execute scripts from folder ```sql``` in following order:
```
1. create.sql
2. fill.sql
```

# Application settings
### Storing files
Uploaded files are stored on file system. You need to specify file path in ```src\main\resources\fileUpload.properties```file using property ```uploadPath```.

### Database credentials
In order to connect to the database you need to specify database credentials in ```src\main\webapp\META-INF\context.xml``` file using ```username``` and ```password``` attributes. By default it is:
```properties
username=root
password=root
```

# Launching application
To launch application open command line and navigate to project's root (where pom.xml is located). Execute command:
```
mvnw.cmd clean package tomcat7:run
```
After project is built it will be accessible by the reference 
> http://localhost:9090/contact/
