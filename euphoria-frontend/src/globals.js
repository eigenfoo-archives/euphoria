export const baseUrl = "http://localhost:8080"; //"http://199.98.27.117:8080";

export function verifyUser(cookies, func, idCheck) {
  const cookie = cookies.get("authenticationHash");
  const url = baseUrl + "/api/cookie/" + cookie;

  fetch(url)
  .then(response => response.json())
  .then(data => {
    if(data.cookie === cookie){
      func()
    }
    else {
      alert("You are not authorized to look at this content. Please sign out and sign in again.")
    }
  })
  .catch(err => {
    // Do something for an error here
  })

  return;
}
