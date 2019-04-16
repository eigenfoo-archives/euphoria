import React from 'react'
import {Image, Button, Container, Row, Col} from "react-bootstrap";

class Listings extends React.Component {

  constructor(props, context) {
    super(props);

    this.handleRedirect = this.handleRedirect.bind(this);
    this.handleGet = this.handleGet.bind(this);

    this.Listing = this.Listing.bind(this);
  }

  handleRedirect(path) {
    this.props.history.push(path);
  }

  handleGet(props) {
    let url = "http://localhost:8080/api/posting/random";

    fetch(url)
      .then(function(data) {
        console.log(data);
      });
    return;
  }

  Listing(props) {
    return(
      <div className="floating-container listing-container-scrolling" style={{width:"600px"}}>
        <Container>
          <Row>
            <h1>
              Software Engineer
            </h1>
          </Row>
          <Row>
            <p style={{fontSize:"20px", color:"#AAA"}}>
              Mountain View, CA
            </p>
          </Row>
          <Row>
            <Col sm={4}>
              <Image
                src={require('../images/1.png')}
                style={{height:"30px"}}
              />
            </Col>
            <Col>
              <p>Skill: Python, C++</p>
            </Col>
          </Row>
          <hr/>
          <Row>
            <p>

            </p>
          </Row>
          <Row>
            <Button variant="info" size="lg" block>Apply</Button>
          </Row>
        </Container>
      </div>
    );
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

        <div className="scrolling-container">
          <this.Listing />
          <this.Listing />
          <this.Listing />
          <this.Listing />
          <this.Listing />
          <this.Listing />
        </div>
    </div>
    );
  }
}
export default Listings
