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
    this.handleApply = this.handleApply.bind(this);
    this.readFile = this.readFile.bind(this);
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
      this.setState({postingData: data[0]});
    })
    .catch(err => {
      // Do something for an error here
    })

    return;
  }

  handleApply() {
    return;
  }

  readFile(event) {
    console.log(event.target.files[0]);

    var file = event.target.files[0];
    var reader = new FileReader();
    reader.onload = function(event) {
      // The file's text will be printed here
      console.log(event.target.result)
    };

    reader.readAsText(file);
  }

  render() {
    const postingData = this.state.postingData;

    if(postingData.skillLevel){
      var skillImage =
      <Image
        src={require("../images/" + postingData.skillLevel + ".png")}
        style={{height:"20px"}}
      />;
    }

    return(
      <div>
        <div className="navbar">
          <div className="logo">
            <Image
              src={require('../images/Logo.png')}
              fluid
              onClick={() => this.handleRedirect("/postings")}
            />
          </div>
        </div>

        <div className="floating-container centered-container" style={{width:"900px"}}>
          <Container fluid>
            <Row>
              <Col sm={9}>
                <h1>
                  {postingData.jobTitle}
                </h1>
              </Col>
              <Col sm={3}>
                <Button
                  variant="info"
                  size="lg"
                  onClick={() => document.getElementById('resumeInput').click()}>
                    Resume
                </Button>
                <input
                  type="file"
                  accept=".pdf"
                  id="resumeInput"
                  name="resume"
                  style={{display:"none"}}
                  onChange={event => this.readFile(event)}/>
              </Col>
            </Row>
            <Row>
              <Col sm={9}>
                <p style={{fontSize:"20px", color:"#AAA"}}>
                  {postingData.location}
                </p>
              </Col>
              <Col sm={3}>
                <Button
                  variant="info"
                  size="lg"
                  onClick={() => document.getElementById('coverLetterInput').click()}>
                    Cover Letter
                  </Button>
                <input
                  type="file"
                  accept=".pdf"
                  id="coverLetterInput"
                  name="coverLetter"
                  style={{display:"none"}}
                  onChange={event => this.readFile(event)}/>
              </Col>
            </Row>
            <Row>
              <Col>
                <p style={{fontSize:"15px", color:"#AAA"}}>
                  {postingData.industry}
                </p>
              </Col>
              <Col>
                {skillImage}
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
              <Button
                variant="info"
                size="lg"
                block
                onClick={() => this.handleApply()}>
                  Apply
                </Button>
            </Row>
          </Container>
        </div>
    </div>

    );
  }
}
export default Apply
