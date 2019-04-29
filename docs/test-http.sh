#!/bin/bash
#
# This script sends test HTTP requests from the command line via curl. Expected
# outputs are documented in `http-expected-output.txt`. Note that the expected
# outputs are produced by running this script immediately after initializing the
# database (with `initialize-database.sql`). Also note that some outputs are
# non-deterministic and will change (e.g. the cookie that is produced upon
# authentication).

curl -X GET http://localhost:8080/api/authentication/apple/hash
echo ""

curl -X GET http://localhost:8080/api/posting/1
echo ""

curl -X GET http://localhost:8080/api/posting/all
echo ""

curl -X GET http://localhost:8080/api/posting/random
echo ""

curl -X GET http://localhost:8080/api/posting/company/123
echo ""

curl -X GET http://localhost:8080/api/posting/NEWYORK/TECHNOLOGY/INTERNSHIP
echo ""

curl -X GET http://localhost:8080/api/application/1
echo ""

curl -X GET http://localhost:8080/api/application/posting/3
echo ""

curl -X GET http://localhost:8080/api/user/2
echo ""

curl -X GET http://localhost:8080/api/company/2
echo ""

curl -X POST http://localhost:8080/api/authentication -H "Content-Type: application/json" -d '{"id": 3, "username": "george", "passwordHash":"ho", "isUser" : true}'
echo ""

curl -X GET http://localhost:8080/api/authentication/george/ho
echo ""

curl -X POST http://localhost:8080/api/posting -H "Content-Type: application/json" -d '{"companyId": 5, "jobTitle": "hello", "description":"there", "location" : "NEWYORK" , "industry": "FINANCE", "skillLevel":"INTERNSHIP"}'
echo ""

curl -X GET http://localhost:8080/api/posting/4
echo ""

curl -X POST http://localhost:8080/api/application -H "Content-Type: application/json" -d '{"postingId": 3, "userId": 2, "resume": "54686973", "coverLetter": "54686973"}'
echo ""

curl -X POST http://localhost:8080/api/application -H "Content-Type: application/json" -d '{"postingId": 1, "userId": 1, "resume": "This", "coverLetter": "This"}'
echo ""

curl -X GET http://localhost:8080/api/application/4
echo ""

curl -X POST http://localhost:8080/api/user -H "Content-Type: application/json" -d '{"name": "george", "email": "ho@cooper.edu", "phoneNumber": "1234567890", "educationLevel": "BACHELORS", "description": "fml."}'
echo ""

curl -X GET http://localhost:8080/api/user/4
echo ""

curl -X POST http://localhost:8080/api/company -H "Content-Type: application/json" -d '{"name": "Cooper", "website": "cooper.edu", "description": "fml."}'
echo ""

curl -X PUT http://localhost:8080/api/posting -H "Content-Type: application/json" -d '{"postingId": 4, "jobTitle": "again hello", "description":"there", "location" : "NEWYORK" , "industry": "FINANCE", "skillLevel":"INTERNSHIP"}'
echo ""

curl -X GET http://localhost:8080/api/posting/4
echo ""

curl -X DELETE http://localhost:8080/api/posting/4
echo ""

curl -X GET http://localhost:8080/api/posting/4
echo ""

curl -X POST http://localhost:8080/api/cookie -H "Content-Type: application/json" -d '{"username": "timapple", "passwordHash": "hash"}'
echo ""

curl -X GET http://localhost:8080/api/cookie/abcdefgh-ijkl-mnop-qurs-tuvwxyz12345
echo ""
