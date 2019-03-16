# Euphoria

A simple recruiting platform web application, written for ECE366 at The Cooper Union.

Contributors: [George Ho](https://github.com/eigenfoo), [Wendy Ide](https://github.com/wside), [Luka Lipovac](https://github.com/lipovac) and [Ostap Voynarovskiy](https://github.com/ostapstephan).

## Database Instructions   

### creating a DB dump file      
create database example;                                //create a db and put stuff in it      
mysqldump -u root -p example > d:\db\example.sql;       //choose directory for dump file      

### receiving a DB on your local computer 
create database receiveDB;                           //create database you want to copy data into    
mysql -u root -p receiveDB < d:\db\example.sql;      //use any dump file     


### additional commands for MySQL  client:    
mysql -u root -p          //'run' mysql   
show databases;           //list all DBs   
show tables;              //list all tables    
describe tablenamehere;   //show table details    
drop table tablenamehere; //del selected table    


