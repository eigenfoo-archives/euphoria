# Euphoria

A simple recruiting platform web application, written for ECE366 at The Cooper Union.

Contributors: [George Ho](https://github.com/eigenfoo), [Wendy Ide](https://github.com/wside), [Luka Lipovac](https://github.com/lipovac) and [Ostap Voynarovskiy](https://github.com/ostapstephan).

## Database Instructions   

### creating a DB dump file      
create database example;                                     
mysqldump -u root -p example > d:\db\example.sql;             

### receiving a DB on your local computer 
create database receiveDB;                           
mysql -u root -p receiveDB < d:\db\example.sql;        


### additional commands for MySQL  client:    
mysql -u root -p&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; //start mysql             
show databases;              
show tables;                
describe tablenamehere;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; //show table details  
drop table tablenamehere;&nbsp; &nbsp; &nbsp; //delete a table
