# Example http requests

1. Authenticate user (`GET` request).

   ` /api/authentication/<username>/<passwordHash>`

   Payload contains nothing.

2. Get posting (`GET` request).

   `/api/posting/<postingId>`

   Payload contains nothing.

3. Get all postings (`GET` request).

   `/api/posting/all`

   Payload contains nothing.

4. Get random postings (`GET` request).

   `/api/posting/random`

   Payload contains nothing.

5. Search postings by location, industry and skill level (`GET` request).

   `/api/posting/<location>/<industry>/<skillLevel>`

   Payload contains nothing.

6. Get application (`GET` request).

   `/api/application/<applicationId>`

   Payload contains nothing.

7. Get all applications to a certain posting (`GET` request).

   `/api/application/posting/<postingId>`

   Payload contains nothing.

8. Get a user (`GET` request).

   `/api/user/<userId>`

   Payload contains nothing.

9. Get a company (`GET` request).

   `/api/company/<companyId>`

   Payload contains nothing.

10. Create a new authentication (`POST` request).

    `/api/authentication`

    Payload contains `id` (e.g. the associated userId or companyId), `username` (e.g. `"timapple"`), `passwordHash` (e.g.
    `"10vfcma3"`) and `isUser` (e.g. `True`).

11. Create a new posting (`POST` request).

    `/api/posting`

    Payload contains `companyId` (e.g. `1`), `jobTitle` (e.g. `"Software
    Engineer"`), `description` (e.g. `"Engineering the softest of wares."`),
    `location` (e.g. `"NEWYORK"`), `industry` (e.g. `"FINANCE"`) and `skillLevel`
    (e.g. `"INTERNSHIP"`).

12. Create a new application (`POST` request).

    `/api/application`

    Payload contains `postingId` (e.g. `1`), `userId` (e.g. `1`), `resume` (some
    hex representation of a file) and `coverLetter` (another hex string string
    representation of a file).

13. Create a new user (`POST` request).

    `/api/user`

    Payload contains `name` (e.g. `"John Smith"`), `email` (e.g.
    `"john@smith.com"`), `phoneNumber` (e.g. `"123-456-7890"`; careful that this
    is a string, not a number!), `educationLevel` (e.g. `"BACHELORS"`) and
    `description` (e.g. `"Am engineer pls hire."`).

14. Create a new company (`POST` request).

    `/api/company`

    Payload contains `name` (e.g. `"Boeing"`), `website` (e.g. `"boeing.com"`)
    and `description` (e.g. `"Only the safest of airplanes."`).

15. Edit a posting (`PUT` request).

    `/api/posting`

    Payload contains `postingId` (e.g. `1`), `jobTitle` (e.g. `"Software
    Engineer"`), `description` (e.g. `"Engineering the softest of wares."`),
    `location` (e.g. `"NEWYORK"`), `industry` (e.g. `"FINANCE"`) and
    `skillLevel` (e.g. `"INTERNSHIP"`). Be careful that unlike creating a
    posting you must pass the `postingId`, _not_ `companyId`, to edit a posting!

16. Delete a posting (`DELETE` request).

    `/api/posting/<postingId>`

    Payload contains nothing.

17. Create a new cookie (`POST` request).

    `/api/cookie/`

    Payload contains `username` (e.g. `"timapple"`), `passwordHash`
    (e.g.`"10vfcma3"`)

18. Get a cookie (`GET` request).

    `/api/cookie/<cookieCheck>`

    Payload contains nothing.

---
To base64 dump any file:
```bash
base64 resume.pdf | tr -d '\n'
```

To hexdump any file:
```bash
xxd -p resume.pdf | tr -d '\n'
```

To decode base64 into files save base64 string in document example then run:   
```bash
base64 --decode example > resumeretreived.pdf   
```
