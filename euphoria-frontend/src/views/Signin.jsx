import React from "react"
//import {withRouter} from "react-router-dom";
import {Image, Form, Button} from "react-bootstrap";

class Signin extends React.Component {

  constructor(props, context) {
    super(props);

    this.handleRedirect = this.handleRedirect.bind(this);
  }

  handleRedirect(path) {
    this.props.history.push(path);
  }

  render() {
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
          <Form onSubmit={e => this.handleSubmit(e)}>
            <Form.Group controlId="formBasicEmail">
              <Form.Label>Email address</Form.Label>
              <Form.Control
                required
                type="username"
                placeholder="Username"
              />
            </Form.Group>

            <Form.Group controlId="formBasicPassword">
              <Form.Label>Password</Form.Label>
              <Form.Control
                required
                type="password"
                placeholder="Password"
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
