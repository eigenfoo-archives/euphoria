import React from "react";
//import {withRouter} from 'react-router-dom';
import {Image, Form, ButtonToolbar, ToggleButton, ToggleButtonGroup, Button, Col} from "react-bootstrap";

class Signup extends React.Component {

  constructor(props, context) {
    super(props);

    this.state = {
          username: "",
          password: "",
          fullname: "",
          education: "",
          email: "",
          phonenumber: "",
          description: ""
    }

    this.handleChange = this.handleChange.bind(this);
    this.handleRedirect = this.handleRedirect.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }



  handleChange(event) {

    var data = event.target.value;

    data = data.replace(/ /g, "%20");

    this.setState({ [event.target.name]: event.target.value});
  }

  handleRedirect(path) {
    this.props.history.push(path);
  }

  handleSubmit(event) {
    const form = event.currentTarget;

    if (form.checkValidity() === false) {
      event.preventDefault();
      event.stopPropagation();
    }

    const {
      username,
      password,
      fullname,
      education,
      email,
      phonenumber,
      description
    } = this.state;


    const user_url = "http://localhost:8080/user/" + fullname + "/" + email + "/" + phonenumber + "/" + education.toUpperCase() + "/"  + description;
    const authentication_url = "http://localhost:8080/authentication/" + username + "/"  + password;

    fetch(user_url, {method: "POST"})
      /*.then(function() {
        fetch(authentication_url, {method: "POST"});
      })*/ //FIXME add support for authentication posting when its implemented
      .then(this.handleRedirect("/login"));



    //this.setState({ validated: true });
  }

  render() {
    const {
      username,
      password,
      fullname,
      education,
      email,
      phonenumber,
      description
    } = this.state;

    return(
      <div>
        <div className="logo">
          <Image src={require('../images/Logo.png')} fluid/>
        </div>

        <div className="container" style={{width:"600px"}}>
          <div className="input">
            <h1>Signup</h1>
            <hr></hr>
            <Form onSubmit={event => this.handleSubmit(event)}>
              <Form.Row>
                <Form.Group as={Col} controlId="formGridUsername">
                  <Form.Label>Username</Form.Label>
                  <Form.Control
                    required
                    type="username"
                    placeholder="Username"
                    name="username"
                    value={username}
                    maxLength="24"
                    onChange={this.handleChange}/>
                  <Form.Control.Feedback type="invalid">
                    Please choose a username.
                  </Form.Control.Feedback>
                </Form.Group>

                <Form.Group as={Col} controlId="formGridUserType">
                  <Form.Label>User Type</Form.Label>
                  <ButtonToolbar>
                    <ToggleButtonGroup
                      type="radio"
                      name="options"
                      defaultValue={0}
                      style={{width:"100%"}}>
                      <ToggleButton variant="info" value={0}>Applicant</ToggleButton>
                      <ToggleButton variant="info" value={1}>Company</ToggleButton>
                    </ToggleButtonGroup>
                  </ButtonToolbar>
                </Form.Group>
              </Form.Row>

              <Form.Group controlId="formGridPassword">
                <Form.Label>Password</Form.Label>
                <Form.Control
                  type="password"
                  name="password"
                  value={password}
                  placeholder="Password"
                  maxLength="48"
                  minLength="8"
                  onChange={this.handleChange}/>
                <Form.Control.Feedback type="invalid">
                  Please choose a password.
                </Form.Control.Feedback>
              </Form.Group>

              <hr/>

              <Form.Row>
                <Form.Group as={Col} controlId="formGridFullName">
                  <Form.Label>Full Name</Form.Label>
                  <Form.Control
                    required
                    type="name"
                    name="fullname"
                    value={fullname}
                    placeholder="Full Name"
                    onChange={this.handleChange}/>
                </Form.Group>

                <Form.Group as={Col} controlId="formGridEducation">
                  <Form.Label>Education Level</Form.Label>
                  <Form.Control
                    required
                    as="select"
                    name="education"
                    value={education}
                    onChange={this.handleChange}>
                    <option>Choose...</option>
                    <option>NoHighschool</option>
                    <option>Highschool</option>
                    <option>GED</option>
                    <option>SomeCollege</option>
                    <option>Associates</option>
                    <option>Bachelors</option>
                    <option>Masters</option>
                    <option>PhD</option>
                    <option>MD</option>
                    <option>JD</option>
                  </Form.Control>
                </Form.Group>
              </Form.Row>

              <Form.Row>
                <Form.Group as={Col} controlId="formGridEmail">
                  <Form.Label>Email</Form.Label>
                  <Form.Control
                    required
                    type="email"
                    placeholder="Email"
                    name="email"
                    value={email}
                    onChange={this.handleChange}/>
                </Form.Group>

                <Form.Group as={Col} controlId="formGridPhone">
                  <Form.Label>Phone Number</Form.Label>
                  <Form.Control
                    required
                    type="phonenumber"
                    name="phonenumber"
                    value={phonenumber}
                    placeholder="5552521956"
                    minLength="10"
                    onChange={this.handleChange}/>
                </Form.Group>
              </Form.Row>

              <Form.Group controlId="formGridDescription">
                <Form.Label>Description</Form.Label>
                <Form.Control
                  required
                  as="textarea"
                  name="description"
                  value={description}
                  maxLength="5000"
                  placeholder="Descrption..."
                  rows="5"
                  style={{resize:"none"}}
                  onChange={this.handleChange}/>
              </Form.Group>

              <Button variant="info" type="submit">
                Submit
              </Button>
              <Button variant="link" type="button" onClick={() => this.handleRedirect("/login")}>
                Sign in...
              </Button>
            </Form>
          </div>
        </div>
      </div>
    );
  }
}
export default Signup
