-- Initializes database and tables.
-- To run: `cat create-database.sql | mysql -u root -p`

drop database if exists euphoria;

drop user if exists 'euphoria'@'localhost';

create database euphoria;

use euphoria;

set global time_zone = '-5:00';

create user 'euphoria'@'localhost' identified with mysql_native_password by 'euphoria';

grant all privileges on euphoria.* to 'euphoria'@'localhost';

-- educationLevel is one of: "NOHIGHSCHOOL",
--                           "HIGHSCHOOL",
--                           "GED",
--                           "SOMECOLLEGE",
--                           "ASSOCIATES",
--                           "BACHELORS",
--                           "MASTERS",
--                           "PHD",
--                           "MD",
--                           "JD"
create table users (userId INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(40) NOT NULL,
                    email VARCHAR (40) NOT NULL,
                    phoneNumber VARCHAR(20) NOT NULL,
                    educationLevel VARCHAR(20) NOT NULL,
                    description TEXT NOT NULL,
                    dateCreated DATETIME NOT NULL); 

create table companies (companyId INT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(40) NOT NULL,
                        website VARCHAR (40) NOT NULL,
                        description TEXT NOT NULL,
                        dateCreated DATETIME NOT NULL);

-- location is one of "NEWYORK",
--                    "LONDON",
--                    "HONGKONG",
--                    "BERLIN",
--                    "BEIJING",
--                    "WASHINGTON"
-- industry is one of "EDUCATION",
--                    "ENERGY",
--                    "FINANCE",
--                    "FOOD",
--                    "HEALTHCARE",
--                    "INSURANCE",
--                    "MEDIA",
--                    "RETAIL",
--                    "SERVICES",
--                    "TECHNOLOGY",
--                    "TRANSPORT",
--                    "UTILITIES"
-- skillLevel is one of "INTERNSHIP",
--                      "ENTRYLEVEL",
--                      "ASSOCIATE",
--                      "SENIOR",
--                      "DIRECTOR",
--                      "EXECUTIVE"
create table postings (postingId INT AUTO_INCREMENT PRIMARY KEY,
                       companyId INT NOT NULL,  -- FOREIGN KEY(companyId) REFERENCES companies (companyId),
                       jobTitle VARCHAR (30) NOT NULL,
                       description TEXT NOT NULL,
                       location VARCHAR(20) NOT NULL,
                       industry VARCHAR(20) NOT NULL,
                       skillLevel VARCHAR(20) NOT NULL,
                       dateCreated DATETIME NOT NULL);

insert into postings (companyId, jobTitle, description, location, industry, skillLevel, dateCreated)
values (123, 'Frontend Developer', 'Must know React.js.', 'NEWYORK', 'TECHNOLOGY', 'INTERNSHIP', "2019-03-16 23:59:59");

create table applications (applicationId INT AUTO_INCREMENT PRIMARY KEY,
                           postingId INT NOT NULL,  -- FOREIGN KEY(postingId), REFERENCES postings (postingId),
                           userId INT NOT NULL, -- FOREIGN KEY(userId), REFERENCES users (userID), 
                           resume TEXT NOT NULL,
                           coverLetter TEXT NOT NULL);

create table authentication (username VARCHAR(30) NOT NULL,
                             passwordHash VARCHAR(40) NOT NULL,
                             userbool BOOLEAN NOT NULL);
