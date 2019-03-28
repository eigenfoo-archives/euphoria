import React from "react";
//import {withRouter} from 'react-router-dom';
import {Image, Form, ButtonToolbar, ToggleButton, ToggleButtonGroup, Button, Col} from "react-bootstrap";

class Signup extends React.Component {

  constructor(props, context) {
    super(props);

    this.state = {
          isUser: 1,
          username: "",
          companyname: "",
          password: "",
          fullname: "",
          education: "",
          email: "",
          phonenumber: "",
          description: ""
    };

    this.handleChange = this.handleChange.bind(this);
    this.handleUserChange = this.handleUserChange.bind(this);
    this.handleRedirect = this.handleRedirect.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);

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
    let url = "";

    if (form.checkValidity() === false) {
      event.preventDefault();
      event.stopPropagation();
    }

    const {
      isUser,
      username,
      companyname,
      password,
      fullname,
      education,
      email,
      phonenumber,
      description
    } = this.state;


    if (isUser){
      url = "http://localhost:8080/user/" + fullname + "/" + email + "/" + phonenumber + "/" + education.toUpperCase() + "/"  + description;
    }
    else{
      url = "http://localhost:8080/company/" + companyname + "/" + email + "/" + description;
    }
    const authentication_url = "http://localhost:8080/authentication/" + username + "/"  + password;

    fetch(url, {method: "POST"})
      /*.then(function() {
        fetch(authentication_url, {method: "POST"});
      })*/ //FIXME add support for authentication posting when its implemented
      .then(this.handleRedirect("/signin"));

      alert("Account Created");
    //this.setState({ validated: true });
  }

  User(props){
    const {
      fullname,
      education,
      email,
      phonenumber,
      description
    } = this.state;

    return(
      <Form>
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
      </Form>
    );
  }

  Company(props){
    const {
      companyname,
      email,
      description
    } = this.state;

    return(
      <Form>
        <Form.Group controlId="formGridCompanyName">
          <Form.Label>Company Name</Form.Label>
          <Form.Control
            required
            type="name"
            name="companyname"
            value={companyname}
            placeholder="Company Name"
            onChange={this.handleChange}/>
        </Form.Group>

        <Form.Group controlId="formGridEmail">
          <Form.Label>Email</Form.Label>
          <Form.Control
            required
            type="email"
            placeholder="Email"
            name="email"
            value={email}
            onChange={this.handleChange}/>
        </Form.Group>

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
      </Form>
    );
  }

  render() {
    const {
      isUser,
      username,
      password,
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

        <div className="form-container" style={{width:"600px"}}>
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
                    <ToggleButton variant="info" value={1}>Applicant</ToggleButton>
                    <ToggleButton variant="info" value={0}>Company</ToggleButton>
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
