import React, { Component } from 'react';
import {Image, Button, ButtonGroup, Container, Row, Col} from "react-bootstrap";
import * as globals from "../globals.js";

class Dashboard extends Component {
  constructor(props, context) {
    super(props);

    this.dashboardUrl = globals.baseUrl + "/api/posting/company/" + this.props.cookies.get("id")

    this.state = {
      companyPostingsData: [],
    };

    this.handleRedirect = this.handleRedirect.bind(this);
    this.handleGet = this.handleGet.bind(this);
    this.handleDelete = this.handleDelete.bind(this);
    this.handleSignout = this.handleSignout.bind(this);

    this.posting = this.posting.bind(this);
  }

  componentDidMount() {
    globals.verifyUser(this.props.cookies, this.handleGet(this.dashboardUrl));
  }

  handleRedirect(path) {
    this.props.history.push(path);
  }

  handleGet(url) {
    fetch(url)
    .then(response => response.json())
    .then(data => {
      this.setState({companyPostingsData: data});
    })
    .catch(err => {
    })

    return;
  }

  handleDelete(postingId){
    const url = globals.baseUrl + "/api/posting/" + postingId;

    fetch(url, {
        method: "DELETE",
      })
      .then(() => {
        globals.verifyUser(this.props.cookies, this.handleGet(this.dashboardUrl));
      })

    return;
  }

  handleSignout() {
    const cookiesProp = this.props.cookies;

    cookiesProp.remove("username");
    cookiesProp.remove("id");
    cookiesProp.remove("isUser");
    cookiesProp.remove("authenticationHash");

    if (cookiesProp.get("username") === undefined){
      alert("Successfully signed out");

      this.handleRedirect("/");
    }
    else{
      alert("Could not sign out. Try at a different time.");
    }

    return;
  }

  posting(props) {
    const companyPostingData = props.companyPostingData;

    return(
      <div className="floating-container posting-container-scrolling" style={{width:"600px"}}>
        <Container>
          <Row>
            <Col>
              <h1>
                {companyPostingData.jobTitle}
              </h1>
            </Col>
          </Row>
          <Row>
            <Col>
              <p style={{fontSize:"20px", color:"#AAA"}}>
                {companyPostingData.location}
              </p>
            </Col>
          </Row>
          <Row>
            <Col>
              <p style={{fontSize:"15px", color:"#AAA"}}>
                {companyPostingData.industry}
              </p>
            </Col>
            <Col>
              <Image
                src={require("../images/" + companyPostingData.skillLevel + ".png")}
                style={{height:"20px"}}
              />
            </Col>
          </Row>
          <hr/>
          <br/>
          <Row>
            <Col>
              <p>
                {companyPostingData.description}
              </p>
            </Col>
          </Row>
          <br/>
          <Row>
            <Col sm={8}>
              <Button variant="info" size="lg" block onClick={() => this.handleRedirect("/dashboard/applications/" + companyPostingData.postingId)}>
                View Applications
              </Button>
            </Col>
            <Col sm={4}>
              <ButtonGroup>
                <Button variant="secondary" size="lg" onClick={() => this.handleRedirect("/dashboard/post/edit/" + companyPostingData.postingId)}>Edit</Button>
                <Button variant="danger" size="lg" onClick={() => this.handleDelete(companyPostingData.postingId)}>Delete</Button>
              </ButtonGroup>
            </Col>
          </Row>
        </Container>
      </div>
    );
  }

  render() {
    const {
      companyPostingsData,
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

        <div className="signout-container" style={{width:"200px", height:"100px"}}>
          <Button
            variant="dark"
            size="lg"
            block
            onClick={() => this.handleSignout()}>
            Sign Out
          </Button>
        </div>

        <div className="scrolling-container">
          {companyPostingsData.map(companyPostingData => (
            <this.posting key={companyPostingData.postingId} companyPostingData={companyPostingData} />
          ))}
        </div>

        <div className="floating-container postings-container-dropdown" style={{width:"300px", height:"100px"}}>
          <Button
            variant="info"
            size="lg"
            block
            onClick={() => this.handleRedirect("/dashboard/post")}>
            Post New Job
          </Button>
        </div>
    </div>
    );
  }
}
export default Dashboard
