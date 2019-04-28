import React, { Component } from 'react';
import {Image, Button, ButtonGroup, Container, Row, Col} from "react-bootstrap";
import * as globals from "../globals.js";

import Navbar from './Navbar';

class Dashboard extends Component {
  constructor(props, context) {
    super(props);

    this.dashboardUrl = globals.baseUrl + "/api/posting/company/" + this.props.cookies.get("id");

    this.state = {
      companyPostingsData: []
    };

    this.handleGet = this.handleGet.bind(this);
    this.handleDelete = this.handleDelete.bind(this);

    this.posting = this.posting.bind(this);
  }

  componentDidMount() {
    globals.verifyUser(this.props.cookies, this.handleGet(this.dashboardUrl));

    return;
  }

  handleGet(url) {
    fetch(url)
      .then(response => response.json())
      .then(data => {
        this.setState({companyPostingsData: data});
      })
      .catch(err => {
      });

    return;
  }

  handleDelete(postingId) {
    const url = globals.baseUrl + "/api/posting/" + postingId;

    fetch(url, {
        method: "DELETE",
      })
      .then(() => {
        globals.verifyUser(this.props.cookies, this.handleGet(this.dashboardUrl));
      });

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
              <Button variant="info" size="lg" block onClick={() => globals.handleRedirect(this.props, "/dashboard/applications/" + companyPostingData.postingId)}>
                View Applications
              </Button>
            </Col>
            <Col sm={4}>
              <ButtonGroup>
                <Button variant="secondary" size="lg" onClick={() => globals.handleRedirect(this.props, "/dashboard/post/edit/" + companyPostingData.postingId)}>Edit</Button>
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
      companyPostingsData
    } = this.state;

    return(
      <div>
        <Navbar {...this.props}/>

        <div className="signout-container" style={{width:"200px", height:"100px"}}>
          <Button
            variant="dark"
            size="lg"
            block
            onClick={() => globals.handleSignout(this.props)}>
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
            onClick={() => globals.handleRedirect(this.props, "/dashboard/post")}>
            Post New Job
          </Button>
        </div>
    </div>
    );
  }
}
export default Dashboard
