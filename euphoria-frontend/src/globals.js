export const baseUrl = "http://199.98.27.117:8080";

export function handleRedirect(props, path) {
  props.history.push(path);
}

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

export function handleSignout(props) {
  const cookiesProp = props.cookies;

  cookiesProp.remove("username");
  cookiesProp.remove("id");
  cookiesProp.remove("isUser");
  cookiesProp.remove("authenticationHash");

  if (cookiesProp.get("username") === undefined){
    alert("Successfully signed out");

    handleRedirect(props, "/");
  }
  else{
    alert("Could not sign out. Try at a different time.");
  }

  return;
}
