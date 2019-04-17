import React, { Component } from 'react';
import {Image, Button, Container, Row, Col} from "react-bootstrap";

class Apply extends Component {

  constructor(props, context) {
    super(props);

    this.state = {
      postingData: [],
    };

    this.handleRedirect = this.handleRedirect.bind(this);
    this.handleGet = this.handleGet.bind(this);
  }

  componentDidMount() {
    let url = "http://localhost:8080/api/posting/" + this.props.match.params.postingId;
    this.handleGet(url);
  }

  handleRedirect(path) {
    this.props.history.push(path);
  }

  handleGet(url) {
    fetch(url)
    .then(response => response.json())
    .then(data => {
      this.setState({postingData: data});
    })
    .catch(err => {
      // Do something for an error here
    })

    return;
  }

  posting(props) {
    const postingData = props.postingData;
    return(
      <div className="floating-container centered-container" style={{width:"900px"}}>
        <Container fluid>
          <Row>
            <Col sm={9}>
              <h1>
                {postingData.jobTitle}
              </h1>
            </Col>
            <Col sm={3}>
              <Button variant="info" size="lg">Resume</Button>
            </Col>
          </Row>
          <Row>
            <Col sm={9}>
              <p style={{fontSize:"20px", color:"#AAA"}}>
                {postingData.location}
              </p>
            </Col>
            <Col sm={3}>
              <Button variant="info" size="lg">Cover Letter</Button>
            </Col>
          </Row>
          <Row>
            <Col>
              <p style={{fontSize:"15px", color:"#AAA"}}>
                {postingData.industry}
              </p>
            </Col>
            <Col>
              <Image
                src={require("../images/" + postingData.skillLevel + ".png")}
                style={{height:"20px"}}
              />
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
                {postingData.description}
              </p>
            </Col>
          </Row>
          <hr/>
          <Row>
            <Button variant="info" size="lg" block>Apply</Button>
          </Row>
        </Container>
      </div>
    );
  }

  render() {
    const postingData = this.state.postingData;

    console.log(postingData);
    return(
      <div>
        <div className="navbar">
          <div className="logo">
            <Image
              src={require('../images/Logo.png')}
              fluid
              onClick={() => this.handleRedirect("/posting")}
            />
          </div>
        </div>


        {postingData.map(postingData => (
          <this.posting postingData={postingData} />
        ))}
    </div>

    );
  }
}
export default Apply
