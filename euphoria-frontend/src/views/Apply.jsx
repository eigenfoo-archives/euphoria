import React, { Component } from 'react';
import {Image, Button, Container, Row, Col} from "react-bootstrap";
import * as globals from "../globals.js";

import Navbar from './Navbar';

class Apply extends Component {

  constructor(props, context) {
    super(props);

    this.state = {
      postingData: [],
      resume: "",
      coverLetter: ""
    };

    this.handleGet = this.handleGet.bind(this);
    this.handleApply = this.handleApply.bind(this);
    this.readFile = this.readFile.bind(this);
  }

  componentDidMount() {
    let url = globals.baseUrl + "/api/posting/" + this.props.match.params.postingId;

    globals.verifyUser(this.props.cookies, this.handleGet(url));
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

  handleApply() {
    const {
      postingData,
      resume,
      coverLetter
    } = this.state;

    let applicationUrl = globals.baseUrl + "/api/application";

    let applicationPayload = {
      postingId: postingData.postingId,
      userId: this.props.cookies.get("id"),
      resume,
      coverLetter
    };

    fetch(applicationUrl, {
        method: "POST",
        body: JSON.stringify(applicationPayload)
      }) //FIXME add check for is user exists
      .then(response => response.json())
      .then(data => {
        if(data !== undefined && data.length === 0){
          alert("Application successfully submitted!")
          globals.handleRedirect(this.props, "/")
        }
      })
      .catch(err => {
      })

    return;
  }

  readFile(event) {
    var file = event.target.files[0];
    var name = event.target.name
    var reader = new FileReader();

    reader.onload = (event) => {
      const encodedFile = event.target.result.split(",");
      this.setState({[name]: encodedFile[1]});
    };

    reader.readAsDataURL(file);
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

    var applyButton =
    <Button
      variant="secondary"
      size="lg"
      block
      onClick={() => {alert("Pleas upload both Resume and Cover Letter")}}>
        Apply
    </Button>;

    if(this.state.coverLetter!=="" && this.state.resume!==""){
      applyButton =
      <Button
        variant="info"
        size="lg"
        block
        onClick={() => globals.verifyUser(this.props.cookies, this.handleApply())}>
          Apply
      </Button>;
    }


    return(
      <div>
        <Navbar {...this.props}/>

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
                  variant={this.state.resume === "" ? "info" : "secondary"}
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
                  variant={this.state.coverLetter === "" ? "info" : "secondary"}
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
              {applyButton}
            </Row>
          </Container>
        </div>
    </div>

    );
  }
}
export default Apply
