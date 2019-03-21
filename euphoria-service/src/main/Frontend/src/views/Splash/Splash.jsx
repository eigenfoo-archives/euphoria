import React from 'react'
import {Image, Form, ButtonToolbar, ToggleButton, ToggleButtonGroup, Button, Container, Row, Col} from 'react-bootstrap';
import './Splash.css'

class Splash extends React.Component {
  render() {
    return(
      <body>
        <div className="container" style={{width:"600px"}}>
          <Container>
            <Row>
              <Col></Col>
              <Col md="auto">
                <Image src={require('../../images/Logo.png')} fluid/>
              </Col>
              <Col></Col>
            </Row>
            <Row></Row>
            <Row></Row>
            <Row>
              <Col></Col>
              <Col>
                <Button variant="info">Info</Button>
              </Col>
              <Col></Col>
              <Col>
                <Button variant="info">Info</Button>
              </Col>
              <Col></Col>
            </Row>
          </Container>
        </div>
      </body>
    );
}
}
export default Splash
