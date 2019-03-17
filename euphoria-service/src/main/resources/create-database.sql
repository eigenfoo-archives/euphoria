create database euphoria;

use euphoria;

create table users (userId INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(40) NOT NULL,
                    email VARCHAR (40) NOT NULL,
                    phoneNumber VARCHAR(20) NOT NULL,
                    educationLevel ENUM ("NOHIGHSCHOOL",
                                         "HIGHSCHOOL",
                                         "GED",
                                         "SOMECOLLEGE",
                                         "ASSOCIATES",
                                         "BACHELORS",
                                         "MASTERS",
                                         "PHD",
                                         "MD",
                                         "JD") NOT NULL,
                    description TEXT NOT NULL,
                    dateCreated DATETIME NOT NULL); 

create table companies (companyId INT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(40) NOT NULL,
                        website VARCHAR (40) NOT NULL,
                        description TEXT NOT NULL,
                        dateCreated DATETIME NOT NULL);

create table postings (postingId INT AUTO_INCREMENT PRIMARY KEY,
                       companyId INT NOT NULL, FOREIGN KEY(companyId) REFERENCES companies (companyId),
                       jobTitle VARCHAR (30) NOT NULL,
                       description TEXT NOT NULL,
                       location ENUM ("NEWYORK",
                                      "LONDON",
                                      "HONGKONG",
                                      "BERLIN",
                                      "BEIJING",
                                      "WASHINGTON") NOT NULL,
                       industry ENUM ("EDUCATION",
                                      "ENERGY",
                                      "FINANCE",
                                      "FOOD",
                                      "HEALTHCARE",
                                      "INSURANCE",
                                      "MEDIA",
                                      "RETAIL",
                                      "SERVICES",
                                      "TECHNOLOGY",
                                      "TRANSPORT",
                                      "UTILITIES") NOT NULL ,
                       skillLevel ENUM ("INTERNSHIP",
                                        "ENTRYLEVEL",
                                        "ASSOCIATE",
                                        "SENIOR",
                                        "DIRECTOR",
                                        "EXECUTIVE") NOT NULL,
                       dateCreated DATETIME NOT NULL);

create table applications (applicationId INT AUTO_INCREMENT PRIMARY KEY,
                           postingId INT NOT NULL, FOREIGN KEY(postingId) REFERENCES postings (postingId),
                           userId INT NOT NULL, FOREIGN KEY(userId) REFERENCES users (userID), 
                           resume TEXT NOT NULL,
                           coverLetter TEXT NOT NULL);

create table authentication (username VARCHAR(30) NOT NULL,
                             passwordHash VARCHAR(40) NOT NULL,
                             userbool BOOLEAN NOT NULL);
