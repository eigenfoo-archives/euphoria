export const baseUrl = "http://localhost:8080"; //"http://199.98.27.117:8080";

export function verifyUser(cookies, func, id) {
  const cookie = cookies.get("authenticationHash");
  const url = baseUrl + "/api/cookie/" + cookie;

  fetch(url)
  .then(response => response.json())
  .then(data => {
    console.log(id);
    if(id !== undefined)
      if((data.cookie === cookie) && (id === data.id)){
        console.log(id);
        console.log(data.get("id"))
        func()
      }
      else {
        alert("There was an issue processing your request. Please log out and in again.")
      }
    else{
      if(data.cookie === cookie){
        func()
      }
      else {
        alert("You are not authorized to look at this content.")
      }
    }
  })
  .catch(err => {
    // Do something for an error here
  })

  return;
}
