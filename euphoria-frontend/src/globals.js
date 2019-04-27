export const baseUrl = "http://localhost:8080"; //"http://199.98.27.117:8080";

export function verifyUser(cookies, func, id) {

  if(id === undefined) {
    id = parseInt(cookies.get("id"));
  }

  const cookie = cookies.get("authenticationHash");
  const url = baseUrl + "/api/cookie/" + cookie;

  fetch(url)
  .then(response => response.json())
  .then(data => {
    if((data.cookie === cookie) && (id === data.id)){
      func()
    }
    else {
      alert("There was an issue processing your request. Please log out and in again.")
    }
  })
  .catch(err => {
    // Do something for an error here
  })

  return;
}
