import React, { Component } from 'react';
import {Image, Button, Container, Row, Col} from "react-bootstrap";

class Applications extends Component {
  constructor(props, context) {
    super(props);

    this.state = {
      applications_data: [],
      location: "",
      industry: "",
      skillLevel: ""
    };

    this.handleRedirect = this.handleRedirect.bind(this);
    this.handleGet = this.handleGet.bind(this);

    this.application = this.application.bind(this);
  }

  componentDidMount() {
    const url = "http://localhost:8080/api/application/posting/" + this.props.match.params.postingId;
    console.log(url);
    this.handleGet(url);
  }

  handleRedirect(path) {
    this.props.history.push(path);
  }

  handleGet(url) {
    fetch(url)
    .then(response => {
      return response.json()
    })
    .then(data => {
      this.setState({applications_data: data});
    })
    .catch(err => {
      // Do something for an error here
    })

    return;
  }

  application(props) {
    const application_data = props.application_data;

    return(
      <div className="floating-container posting-container-scrolling" style={{width:"600px"}}>
        <Container>
          <Row>
            <Col>
              <h1>
                {application_data.name}
              </h1>
            </Col>
          </Row>
          <Row>
            <Col>
              <p style={{fontSize:"20px", color:"#AAA"}}>
                {application_data.email}
              </p>
            </Col>
            <Col>
              <p style={{fontSize:"20px", color:"#AAA"}}>
                {application_data.phoneNumber}
              </p>
            </Col>
          </Row>
          <hr/>
          <Row>
            <Button variant="info" size="lg" block>Download Documents</Button>
          </Row>
        </Container>
      </div>
    );
  }

  render() {
    const {
      applications_data,
      location,
      industry,
      skillLevel,
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

        <div className="scrolling-container">
          {applications_data.map(application_data => (
            <this.application key={application_data.postingId} application_data={application_data} />
          ))}
        </div>
    </div>
    );
  }
}
export default Applications
