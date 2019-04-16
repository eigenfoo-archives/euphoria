import React from 'react'
import {Image, Button, Container, Row, Col} from "react-bootstrap";


class Apply extends React.Component {
  constructor(props, context) {
    super(props);

    this.handleRedirect = this.handleRedirect.bind(this);
    this.handleGet = this.handleGet.bind(this);
  }

  handleRedirect(path) {
    this.props.history.push(path);
  }

  handleGet(props) {
    let url = "http://localhost:8080/api/posting/";

    fetch(url)
      .then(function(data) {
        console.log(data);
      });
    return;
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

        <div className="floating-container centered-container" style={{width:"900px"}}>
          <Container fluid>
            <Row>
              <Col>
                <h1>
                  Software Engineer
                </h1>
              </Col>
            </Row>
            <Row>
              <Col>
                <p style={{fontSize:"20px", color:"#AAA"}}>
                  Mountain View, CA
                </p>
              </Col>
            </Row>
            <Row>
              <Col sm={4}>
                <p style={{fontSize:"16px", color:"#AAA"}}>
                  Level
                </p>
                <Image
                  src={require('../images/1.png')}
                  style={{height:"30px"}}
                />
              </Col>
              <Col>
                <p style={{fontSize:"16px", color:"#AAA"}}>
                  Skills
                </p>
                <p>Python, C++</p>
              </Col>
            </Row>
            <br/>
            <Row>
              <Col>
                <p style={{fontSize:"16px", color:"#AAA"}}>
                  Description
                </p>
              </Col>
            </Row>
            <Row>
              <Col>
                <p>
                  Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
                </p>
              </Col>
            </Row>
            <hr/>
            <Row>
              <Button variant="info" size="lg" block>Apply</Button>

          </Row>
          </Container>
        </div>
    </div>
    );
  }
}
export default Apply
