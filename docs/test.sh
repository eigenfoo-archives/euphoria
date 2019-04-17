curl -X GET http://localhost:8080/api/authentication/apple/hash
# [{"id":1,"username":"apple","passwordHash":"hash","isUser":false}]%

curl -X GET http://localhost:8080/api/posting/1
# [{"postingId":1,"companyId":123,"jobTitle":"Underwater Basket Weaver","description":"Must lift.","location":"WASHINGTON","industry":"SERVICES","skillLevel":"INTERNSHIP","dateCreated":"2018-07-10 02:30:00"}]%

curl -X GET http://localhost:8080/api/posting/all
# [{"postingId":1,"companyId":123,"jobTitle":"Underwater Basket Weaver","description":"Must lift.","location":"WASHINGTON","industry":"SERVICES","skillLevel":"INTERNSHIP","dateCreated":"2018-07-10 02:30:00"},{"postingId":2,"companyId":456,"jobTitle":"Frontend Developer","description":"Must know everything about React.js.","location":"NEWYORK","industry":"TECHNOLOGY","skillLevel":"EXECUTIVE","dateCreated":"2019-02-12 12:00:00"},{"postingId":3,"companyId":789,"jobTitle":"Backend Developer","description":"Must know nothing about React.js.","location":"NEWYORK","industry":"TECHNOLOGY","skillLevel":"INTERNSHIP","dateCreated":"2019-03-16 23:59:59"}]%

curl -X GET http://localhost:8080/api/posting/random
# Output changes

curl -X GET http://localhost:8080/api/posting/company/123
# [{"postingId":1,"companyId":123,"jobTitle":"Underwater Basket Weaver","description":"Must lift.","location":"WASHINGTON","industry":"SERVICES","skillLevel":"INTERNSHIP","dateCreated":"2019-04-16 02:12:22"}]

curl -X GET http://localhost:8080/api/posting/NEWYORK/TECHNOLOGY/INTERNSHIP
# [{"postingId":3,"companyId":789,"jobTitle":"Backend Developer","description":"Must know nothing about React.js.","location":"NEWYORK","industry":"TECHNOLOGY","skillLevel":"INTERNSHIP","dateCreated":"2019-03-16 23:59:59"}]%

curl -X GET http://localhost:8080/api/application/1
# [{"applicationId":1,"postingId":1,"userId":1,"resume":"VGhpcw==","coverLetter":"VT16NA==","dateCreated":"2018-07-11 05:30:00"}]%

curl -X GET http://localhost:8080/api/application/posting/3
# [{"applicationId":3,"postingId":3,"userId":3,"resume":"ZT16OA==","coverLetter":"ZT56OQ==","dateCreated":"2019-02-13 11:40:33"}]%

curl -X GET http://localhost:8080/api/user/2
# [{"userId":2,"name":"Tim Apple","email":"tim@apple.com","phoneNumber":"456-123-7890","educationLevel":"MASTERS","description":"I also like Macintoshes."}]%

curl -X GET http://localhost:8080/api/company/2
# [{"companyId":2,"name":"Amazon","website":"bozo.com","description":"Not in Queens."}]%

curl -X POST http://localhost:8080/api/authentication -H "Content-Type: application/json" -d '{"id": 3, "username": "george", "passwordHash":"ho", "isUser" : true}'
# []%

curl -X GET http://localhost:8080/api/authentication/george/ho
# [{"id":3,"username":"george","passwordHash":"ho","isUser":true}]%

curl -X POST http://localhost:8080/api/posting -H "Content-Type: application/json" -d '{"companyId": 5, "jobTitle": "hello", "description":"there", "location" : "NEWYORK" , "industry": "FINANCE", "skillLevel":"INTERNSHIP"}'
# []%

curl -X GET http://localhost:8080/api/posting/4
# [{"postingId":4,"companyId":5,"jobTitle":"hello","description":"there","location":"NEWYORK","industry":"FINANCE","skillLevel":"INTERNSHIP","dateCreated":"2019-04-13 16:25:03"}]%

curl -X POST http://localhost:8080/api/application -H "Content-Type: application/json" -d '{"postingId": 3, "userId": 2, "resume": x"54686973", "coverLetter": x"54686973"}'
# []%

curl -X POST http://localhost:8080/api/application -H "Content-Type: application/json" -d '{"postingId": 1, "userId": 1, "resume": "This", "coverLetter": "This"}'
# []%

curl -X GET http://localhost:8080/api/application/4
# [{"applicationId":4,"postingId":1,"userId":1,"resume":"VGhpcw==","coverLetter":"VGhpcw==","dateCreated":"2019-04-13 16:30:42"}]%

curl -X POST http://localhost:8080/api/user -H "Content-Type: application/json" -d '{"name": "george", "email": "ho@cooper.edu", "phoneNumber": "1234567890", "educationLevel": "BACHELORS", "description": "fml."}'
# [{"userId":4,"name":"namefield","email":"emailfield","phoneNumber":"phoneNumfield","educationLevel":"PHD","description":"descriptonfield"}]%

curl -X GET http://localhost:8080/api/user/4
# [{"userId":4,"name":"george","email":"ho@cooper.edu","phoneNumber":"1234567890","educationLevel":"BACHELORS","description":"fml."}]%

curl -X POST http://localhost:8080/api/company -H "Content-Type: application/json" -d '{"name": "Cooper", "website": "cooper.edu", "description": "fml."}'
# [{"companyId":3,"name":"namefield","website":"websitefield","description":"descriptonfield"}]%

curl -X PUT http://localhost:8080/api/posting -H "Content-Type: application/json" -d '{"postingId": 4, "jobTitle": "again hello", "description":"there", "location" : "NEWYORK" , "industry": "FINANCE", "skillLevel":"INTERNSHIP"}'
# []%

curl -X GET http://localhost:8080/api/posting/4
# [{"postingId":4,"companyId":5,"jobTitle":"again hello","description":"there","location":"NEWYORK","industry":"FINANCE","skillLevel":"INTERNSHIP","dateCreated":"2019-04-13 16:25:03"}]%

curl -X DELETE http://localhost:8080/api/posting/4
# []%

curl -X GET http://localhost:8080/api/posting/4
# [null]%

curl -X POST http://localhost:8080/api/cookie -H "Content-Type: application/json" -d '{"username": "timapple", "passwordHash": "hash"}'
# [{"cookie":"random36charUUIDstring"}]%

curl -X GET http://localhost:8080/api/cookie/specific36charUUIDstring
# [{"cookie":"specific36charUUIDstring"}]%
