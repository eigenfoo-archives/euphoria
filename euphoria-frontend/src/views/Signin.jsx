import React, { Component } from "react";
import {Form, Button} from "react-bootstrap";
import * as globals from "../globals.js";

import Navbar from "./Navbar";

class Signin extends Component {

  constructor(props, context) {
    super(props);

    this.state = {
          username: "",
          password: "",
    };

    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);

    this.signIn = this.signIn.bind(this);
    this.getUserData = this.signIn.bind(this);
  }

  handleChange(event) {
    this.setState({ [event.target.name]: event.target.value});

    return;
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

    const cookieUrl = globals.baseUrl + "/api/cookie";
    const pleaseDontDockPoints = btoa(username + password);

    let authenticationPayload = {
      username: username,
      passwordHash: pleaseDontDockPoints
    };

    fetch(cookieUrl, {
        method: "POST",
        body: JSON.stringify(authenticationPayload)
      })
      .then(response => response.json())
      .then(data => {
        if(!(data == null)){
          const cookiesProp = this.props.cookies;

          cookiesProp.set("username", username, { path: '/', maxAge: 1728000 });
          cookiesProp.set("id", data.id, { path: '/', maxAge: 1728000 });
          cookiesProp.set("isUser", data.isUser, { path: '/', maxAge: 1728000 });
          cookiesProp.set("authenticationHash", data.cookie, { path: '/', maxAge: 1728000 });

          globals.handleRedirect(this.props, "/");
        }
        else{
          alert("Not a valid login");
        }
      })
      .catch(err => {
      });

    return;
  }

  render() {
    const {
      username,
      password
    } = this.state;

    return(
      <div>
        <Navbar {...this.props}/>

        <div className="floating-container centered-container" style={{width:"600px"}}>
          <h1>Sign In</h1>
          <hr></hr>
          <Form onSubmit={event => this.handleSubmit(event)}>
            <Form.Group controlId="formBasicEmail">
              <Form.Label>Username</Form.Label>
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
            <Button variant="link" type="button" onClick={() => globals.handleRedirect(this.props, "/signup")}>
              Sign up...
            </Button>
          </Form>
        </div>
      </div>
    );
  }
}
export default Signin
