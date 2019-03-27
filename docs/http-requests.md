# Example http requests

```bash
# Get user with username timapple and passwordHash hash.
curl -X GET http://localhost:8080/authentication/timapple/hash

# Get posting with postingId 1:
curl -X GET http://localhost:8080/posting/1

# Get all postings:
curl -X GET http://localhost:8080/posting/getAll

# Get all postings to posting with postingId 1
curl -X GET http://localhost:8080/application/matchPosting/1

# Create a new posting
curl -X POST http://localhost:8080/posting/420/Hello/there/NEWYORK/FINANCE/INTERNSHIP

# Create a new user
curl -X POST http://localhost:8080/user/John%20Smith/jsmith@gmail.com/8773934448/BACHELORS/Am%20engineer%20pls%20hire.

# Create a new company
curl -X POST http://localhost:8080/company/Boeing/boeing.com/Oops

# Create a new application
curl -X POST http://localhost:8080/application/123/400/1/hexstring1/hexstring2  --hex string MUST be even numbers of chars. Use hex representation of a file.

# Create a new authentication
curl -X POST http://localhost:8080/authentication/janedoe/hash/TRUE
    
# Edit a posting
curl -X PUT http://localhost:8080/posting/1/Intern/Updated%20description./NEWYORK/FINANCE/ENTRYLEVEL
```

To hexdump any file:
```bash
xxd -p resume.txt | tr -d '\n'
```
To decode base64 into files save base64 string in document example then run:   
```bash
base64 --decode example > resumeretreived.txt   
```
