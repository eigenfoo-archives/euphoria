import React from 'react'
import {Navbar, Image, Form, Button} from 'react-bootstrap';
import './Login.css'

class Login extends React.Component {
  render() {
    return(
      <body>
        <div class="logo">
          <Image src={require('../../images/Logo.png')} fluid/>
        </div>

        <div class="container" style={{width:"600px"}}>
          <div class="input">
            <h1>Login</h1>
            <hr></hr>
            <Form>
              <Form.Group controlId="formBasicEmail">
                <Form.Label>Email address</Form.Label>
                <Form.Control type="email" placeholder="Enter email" />
              </Form.Group>

              <Form.Group controlId="formBasicPassword">
                <Form.Label>Password</Form.Label>
                <Form.Control type="password" placeholder="Password" />
              </Form.Group>

              <Button variant="info" type="submit">
                Submit
              </Button>
              <Button variant="link" type="button">
                Sign up...
              </Button>
            </Form>
          </div>
        </div>
      </body>
    );
  }
}
export default Login
