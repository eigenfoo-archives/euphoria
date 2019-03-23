# Http request Instructions for JDBC   

curl -X GET http://localhost:8080/posting/1   
curl -X GET http://localhost:8080/posting/getAll   
curl -X GET http://localhost:8080/application/matchPosting/1   

curl -X POST http://localhost:8080/posting/420/Hello/there/NEWYORK/FINANCE/INTERNSHIP   
curl -X POST http://localhost:8080/user/John%20Smith/jsmith@gmail.com/8773934448/BACHELORS/am%20engineer%20pls%20hire    
curl -X POST http://localhost:8080/company/Exxon/exxonmobile.com/yay%20fraking%20is%20great   
curl -X POST http://localhost:8080/application/123/400/1/binary1/binary2                --doesn't work yet  

