import React, { Component } from 'react';
import {Image, Button, ButtonGroup, Container, Row, Col} from "react-bootstrap";

class Dashboard extends Component {
  constructor(props, context) {
    super(props);

    this.dashboardUrl = "http://localhost:8080/api/posting/company/" + this.props.cookies.get("id")

    this.state = {
      companyPostingsData: [],
    };

    this.handleRedirect = this.handleRedirect.bind(this);
    this.handleGet = this.handleGet.bind(this);
    this.handleDelete = this.handleDelete.bind(this);

    this.posting = this.posting.bind(this);
  }

  componentDidMount() {
    // if (this.props.cookies.get("isUser")){
    //   this.handleRedirect("/");
    // }

    this.handleGet(this.dashboardUrl);
  }

  handleRedirect(path) {
    this.props.history.push(path);
  }

  handleGet(url) {
    fetch(url)
    .then(response => response.json())
    .then(data => {
      // Work with JSON data here
      this.setState({companyPostingsData: data});
      console.log(this.state.companyPostingsData);
    })
    .catch(err => {
      // Do something for an error here
    })

    return;
  }

  handleDelete(postingId){
    const url = "http://localhost:8080/api/posting/" + postingId;
    fetch(url, {
        method: "DELETE",
      })
      .then(() => {
        this.handleGet(this.dashboardUrl);
      })

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
              onClick={() => this.handleRedirect("/dashboard")}
            />
          </div>
        </div>

        <div className="scrolling-container">
          {companyPostingsData.map(companyPostingData => (
            <this.posting key={companyPostingData.postingId} companyPostingData={companyPostingData} />
          ))}
        </div>

        <div className="floating-container postings-container-dropdown" style={{width:"300px", height:"100px"}}>
          <Button variant="info" size="lg" block onClick={() => this.handleRedirect("/dashboard/post")}>
            Post New Job
          </Button>
        </div>
    </div>
    );
  }
}
export default Dashboard
