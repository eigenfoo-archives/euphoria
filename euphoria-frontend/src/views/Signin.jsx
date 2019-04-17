import React, { Component } from 'react';
import {Image, Form, Button} from "react-bootstrap";

class Signin extends Component {

  constructor(props, context) {
    super(props);

    this.state = {
          username: "",
          password: "",
    };

    this.handleChange = this.handleChange.bind(this);
    this.handleRedirect = this.handleRedirect.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);

    this.signIn = this.signIn.bind(this);
    this.getUserData = this.signIn.bind(this);
  }

  handleChange(event) {
    this.setState({ [event.target.name]: event.target.value});
  }

  handleRedirect(path) {
    this.props.history.push(path);
  }

  handleSubmit(event) {
    event.preventDefault(); //prevent redirect with form in url

    const form = event.currentTarget;

    if (form.checkValidity() === false) {
      event.preventDefault();
      event.stopPropagation();
    }

    this.signIn();

    return;
  }

  signIn() {
    const {
      username,
      password
    } = this.state;

    const cookieUrl = "http://localhost:8080/api/cookie";

    let authenticationPayload = {
      username: username,
      passwordHash: password,
    };

    fetch(cookieUrl, {
        method: "POST",
        body: JSON.stringify(authenticationPayload)
      })
      .then(response => response.json())
      .then(data => {
        const cookie = data[0];

        if(cookie != null){
          const cookies = this.props.cookies;

          cookies.set("username", username, { path: '/' });
          cookies.set("id", cookies.id, { path: '/' });
          cookies.set("isUser", cookies.isUser, { path: '/' });
          cookies.set("authenticationHash", cookie.cookie, { path: '/' });
          console.log(cookies);
          if(cookies.isUser){
            this.handleRedirect("/postings")
          }
          else{
            this.handleRedirect("/dashboard")
          }
        }
        else{
          alert("Not a valid login");
        }

      })
      .catch(err => {
      })

    return;
  }

  render() {

    const {
      username,
      password
    } = this.state;

    return(
      <div>
        <div className="navbar">
          <div className="logo">
            <Image
              src={require('../images/Logo.png')}
              fluid
              onClick={() => this.handleRedirect("/")}
            />
          </div>
        </div>

        <div className="floating-container centered-container" style={{width:"600px"}}>
          <h1>Sign In</h1>
          <hr></hr>
          <Form onSubmit={event => this.handleSubmit(event)}>
            <Form.Group controlId="formBasicEmail">
              <Form.Label>Email address</Form.Label>
              <Form.Control
                required
                type="username"
                placeholder="Username"
                name="username"
                value={username}
                onChange={this.handleChange}
              />
            </Form.Group>

            <Form.Group controlId="formBasicPassword">
              <Form.Label>Password</Form.Label>
              <Form.Control
                required
                type="password"
                placeholder="Password"
                name="password"
                value={password}
                onChange={this.handleChange}
              />
            </Form.Group>

            <Button variant="info" type="submit">
              Submit
            </Button>
            <Button variant="link" type="button" onClick={() => this.handleRedirect("/signup")}>
              Sign up...
            </Button>
          </Form>
        </div>
      </div>
    );
  }
}
export default Signin
