import React, { Component } from 'react';
import {Image, Form, ButtonToolbar, ToggleButton, ToggleButtonGroup, Button, Col} from "react-bootstrap";

class Signup extends Component {

  constructor(props, context) {
    super(props);

    this.state = {
          isUser: true,
          username: "",
          companyName: "",
          password: "",
          name: "",
          educationLevel: "",
          email: "",
          website: "",
          phoneNumber: "",
          description: ""
    };

    this.handleChange = this.handleChange.bind(this);
    this.handleUserChange = this.handleUserChange.bind(this);
    this.handleRedirect = this.handleRedirect.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);

    this.createUser = this.createUser.bind(this);
    this.createUserAuthentication = this.createUserAuthentication.bind(this);

    this.User = this.User.bind(this);
    this.Company = this.Company.bind(this);
  }

  handleChange(event) {
    var data = event.target.value;

    data = data.replace(/ /g, "%20");

    this.setState({ [event.target.name]: event.target.value});
  }

  handleUserChange(isUser){
    this.setState({isUser});
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

    this.createUser();

    return;
  }

  createUser() {
    const {
      isUser,
      companyName,
      name,
      educationLevel,
      email,
      website,
      phoneNumber,
      description
    } = this.state;

    let userUrl = "http://localhost:8080/api/";
    let userPayload = "";

    if (isUser){
      userUrl += "user";
      userPayload = {
        name,
        email,
        phoneNumber,
        educationLevel,
        description
      };
    }
    else{
      userUrl += "company";
      userPayload = {
        name: companyName,
        website,
        description
      };
    }

    fetch(userUrl, {
        method: "POST",
        body: JSON.stringify(userPayload)
      }) //FIXME add check for is user exists
      .then(response => response.json())
      .then(data => this.createUserAuthentication(data[0].userId))
      .then(this.handleRedirect("/"))
      .catch(err => {
      })

    return;
  }

  createUserAuthentication(userId) {
    const {
      isUser,
      username,
      password,
    } = this.state;

    const authenticationUrl = "http://localhost:8080/api/authentication";

    let authenticationPayload = {
      id: userId,
      username: username,
      passwordHash: password,
      isUser: isUser
    };

    fetch(authenticationUrl, {
        method: "POST",
        body: JSON.stringify(authenticationPayload)
      })
      .then(response => response.json())
      .then(data => {
        if(data.length == 0){
          alert("Account Created");
        }
      })
      .catch(err => {
      });

    return;
  }

  User(props){
    const {
      name,
      educationLevel,
      email,
      phoneNumber,
    } = this.state;

    return(
      <Form>
        <Form.Row>
          <Form.Group as={Col} controlId="formGridFullName">
            <Form.Label>Full Name</Form.Label>
            <Form.Control
              required
              type="name"
              name="name"
              value={name}
              placeholder="Full Name"
              onChange={this.handleChange}/>
          </Form.Group>

          <Form.Group as={Col} controlId="formGridEducation">
            <Form.Label>Education Level</Form.Label>
            <Form.Control
              required
              as="select"
              name="educationLevel"
              value={educationLevel}
              onChange={this.handleChange}>
              <option>Choose...</option>
              <option>NOHIGHSCHOOL</option>
              <option>GED</option>
              <option>HIGHSCHOOL</option>
              <option>SOMECOLLEGE</option>
              <option>ASSOCIATES</option>
              <option>BACHELORS</option>
              <option>MASTERS</option>
              <option>PHD</option>
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
              type="phoneNumber"
              name="phoneNumber"
              value={phoneNumber}
              placeholder="5552521956"
              minLength="10"
              onChange={this.handleChange}/>
          </Form.Group>
        </Form.Row>
      </Form>
    );
  }

  Company(props){
    const {
      companyName,
      website
    } = this.state;

    return(
      <Form>
        <Form.Group controlId="formGridCompanyName">
          <Form.Label>Company Name</Form.Label>
          <Form.Control
            required
            type="name"
            name="companyName"
            value={companyName}
            placeholder="Company Name"
            onChange={this.handleChange}/>
        </Form.Group>

        <Form.Group controlId="formGridWebsite">
          <Form.Label>Email</Form.Label>
          <Form.Control
            required
            type="website"
            placeholder="Website"
            name="website"
            value={website}
            onChange={this.handleChange}/>
        </Form.Group>
      </Form>
    );
  }

  render() {
    const {
      isUser,
      username,
      password,
      description
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
          <h1>Sign Up</h1>
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
                    defaultValue={isUser}
                    style={{width:"100%"}}
                    onChange={this.handleUserChange}>
                    <ToggleButton variant="info" value={true}>Applicant</ToggleButton>
                    <ToggleButton variant="info" value={false}>Company</ToggleButton>
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

            {isUser ? (
              <this.User/>
            ) : (
              <this.Company/>
            )}

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
              <Form.Control.Feedback type="invalid">
                Please write a description.
              </Form.Control.Feedback>
            </Form.Group>

            <Button variant="info" type="submit">
              Submit
            </Button>
            <Button variant="link" type="button" onClick={() => this.handleRedirect("/signin")}>
              Sign in...
            </Button>
          </Form>
        </div>
      </div>
    );
  }
}
export default Signup
