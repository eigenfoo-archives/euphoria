# Example http requests

1. Authenticate user (`GET` request).

   ` /api/authentication/ `

   Payload contains `username` (e.g. `"timapple"`) and `passwordHash` (e.g.
   `"10vfcma3"`).

2. Get posting (`GET` request).

   `/api/posting/single/`

   Payload contains `postingId` (e.g. `1`)

3. Get all postings (`GET` request).

   `/api/posting/all/`

   Payload contains nothing.

4. Get all applications to a certain posting (`GET` request).

   `/api/application/forPosting/`

   Payload contains `postingId` (e.g. `1`).

5. Create a new posting (`POST` request).

   `/api/posting/create/`

   Payload contains `companyId` (e.g. `1`), `jobTitle` (e.g. `"Software
   Engineer"`), `description` (e.g. `"Engineering the softest of wares."`),
   `location` (e.g. `"NEWYORK"`), `industry` (e.g. `"FINANCE"`) and `skillLevel`
   (e.g. `"INTERNSHIP"`).

6. Create a new user (`POST` request).



```bash
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
