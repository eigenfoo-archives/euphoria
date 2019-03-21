import React from 'react'
import {Navbar, Image, Form, ButtonToolbar, ToggleButton, ToggleButtonGroup, Button, Col} from 'react-bootstrap';
import './Signup.css'

class Signup extends React.Component {
  constructor(props, context) {
    super(props);

    this.handleChange = this.handleChange.bind(this);
  }

  handleChange(value, event) {
    //this.setState({ value });
    console.log(value);
  }

  render() {
    return(
      <body>
        <div class="logo">
          <Image src={require('../../images/Logo.png')} fluid/>
        </div>

        <div class="container" style={{width:"600px"}}>
          <div class="input">
            <h1>Signup</h1>
            <hr></hr>
            <Form>
              <Form.Row>
                <Form.Group as={Col} controlId="formGridUsername">
                  <Form.Label>Username</Form.Label>
                  <Form.Control type="username" placeholder="Username" />
                </Form.Group>

                <Form.Group as={Col} controlId="formGridUserType">
                  <Form.Label>User Type</Form.Label>
                  <ButtonToolbar>
                    <ToggleButtonGroup
                      type="radio"
                      name="options"
                      defaultValue={0}
                      onChange={this.handleChange}
                      style={{width:"100%"}}>
                      <ToggleButton variant="info" value={0}>Applicant</ToggleButton>
                      <ToggleButton variant="info" value={1}>Company</ToggleButton>
                    </ToggleButtonGroup>
                  </ButtonToolbar>
                </Form.Group>
              </Form.Row>

              <Form.Row>
                <Form.Group as={Col} controlId="formGridPassword">
                  <Form.Label>Password</Form.Label>
                  <Form.Control type="password" placeholder="Password" />
                </Form.Group>

                <Form.Group as={Col} controlId="formGridConfirmPassword">
                  <Form.Label>Confirm Password</Form.Label>
                  <Form.Control type="confirmpassword" placeholder="Confirm Password" />
                </Form.Group>
              </Form.Row>

              <hr/>

              <Form.Row>
                <Form.Group as={Col} controlId="formGridName">
                  <Form.Label>Full Name</Form.Label>
                  <Form.Control type="name" placeholder="Full Name" />
                </Form.Group>

                <Form.Group as={Col} controlId="formGridEmail">
                  <Form.Label>Email</Form.Label>
                  <Form.Control type="email" placeholder="Email" />
                </Form.Group>
              </Form.Row>

              <Form.Row>
                <Form.Group as={Col} controlId="formGridOccupation">
                  <Form.Label>Occupation</Form.Label>
                  <Form.Control as="select">
                    <option>Choose...</option>
                    <option>...</option>
                  </Form.Control>
                </Form.Group>

                <Form.Group as={Col} controlId="formGridEducation">
                  <Form.Label>Education Level</Form.Label>
                  <Form.Control as="select">
                    <option>Choose...</option>
                    <option>...</option>
                  </Form.Control>
                </Form.Group>

              </Form.Row>

              <Form.Group controlId="formGridDescription">
                <Form.Label>Descrption</Form.Label>
                <Form.Control as="textarea" placeholder="Descrption..." rows="5" style={{resize:"none"}}/>
              </Form.Group>

              <Button variant="info" type="submit">
                Submit
              </Button>
              <Button variant="link" type="button">
                Sign in...
              </Button>
            </Form>
          </div>
        </div>
      </body>
    );
  }
}
export default Signup
