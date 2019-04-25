import React, { Component } from 'react';
import {Image, Button, Container, Row, Col, Form} from "react-bootstrap";

class Postings extends Component {
  constructor(props, context) {
    super(props);

    this.state = {
      postingsData: [],
      location: "...",
      industry: "...",
      skillLevel: "..."
    };

    this.handleChange = this.handleChange.bind(this);
    this.handleRedirect = this.handleRedirect.bind(this);
    this.handleGet = this.handleGet.bind(this);
    this.handleSignout = this.handleSignout.bind(this);

    this.posting = this.posting.bind(this);
  }

  componentDidMount() {
    const url = "http://199.98.27.117:8080/api/posting/random";
    this.handleGet(url);
  }

  handleChange(event) {
    let url = "http://199.98.27.117:8080/api/posting/"

    this.setState({[event.target.name]: event.target.value}, () => {

      const {
        location,
        industry,
        skillLevel,
      } = this.state;

      let filterList = [location, industry, skillLevel];

      for (let i = 0; i < filterList.length; i++){
        if (filterList[i].endsWith("...")){
          url += "/";
        }
        else{
          url += filterList[i] + "/";
        }
      }

      this.handleGet(url);
    });
  }

  handleRedirect(path) {
    this.props.history.push(path);
  }

  handleGet(url) {
    fetch(url)
    .then(response => response.json())
    .then(data => {
      this.setState({postingsData: data});
    })
    .catch(err => {
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
    const postingData = props.postingData;

    return(
      <div className="floating-container posting-container-scrolling" style={{width:"600px"}}>
        <Container>
          <Row>
            <Col>
              <h1>
                {postingData.jobTitle}
              </h1>
            </Col>
          </Row>
          <Row>
            <Col>
              <p style={{fontSize:"20px", color:"#AAA"}}>
                {postingData.location}
              </p>
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
          <hr/>
          <br/>
          <Row>
            <Col>
              <p>
                {postingData.description}
              </p>
            </Col>
          </Row>
          <br/>
          <Row>
            <Button variant="info" size="lg" block onClick={() => this.handleRedirect("/postings/apply/" + postingData.postingId)}>
              Apply
            </Button>
          </Row>
        </Container>
      </div>
    );
  }

  render() {
    const {
      postingsData,
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
              onClick={() => this.handleRedirect("/postings")}
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
          {postingsData.map(postingData => (
            <this.posting key={postingData.postingId} postingData={postingData} />
          ))}
        </div>

        <div className="floating-container postings-container-dropdown" style={{width:"900px", height:"100px"}}>
          <Form>
            <Form.Row>
              <Form.Group as={Col} controlId="formGridLocation">
                <Form.Control
                  required
                  as="select"
                  name="location"
                  value={location}
                  onChange={this.handleChange}>
                  <option>Location...</option>
                  <option>NEWYORK</option>
                  <option>LONDON</option>
                  <option>HONGKONG</option>
                  <option>BERLIN</option>
                  <option>BEIJING</option>
                  <option>WASHINGTON</option>
                </Form.Control>
              </Form.Group>

              <Form.Group as={Col} controlId="formGridIndustry">
                <Form.Control
                  required
                  as="select"
                  name="industry"
                  value={industry}
                  onChange={this.handleChange}>
                  <option>Industry...</option>
                  <option>EDUCATION</option>
                  <option>ENERGY</option>
                  <option>FINANCE</option>
                  <option>FOOD</option>
                  <option>HEALTHCARE</option>
                  <option>INSURANCE</option>
                  <option>MEDIA</option>
                  <option>RETAIL</option>
                  <option>SERVICES</option>
                  <option>TECHNOLOGY</option>
                  <option>TRANSPORT</option>
                  <option>UTILITIES</option>
                </Form.Control>
              </Form.Group>

              <Form.Group as={Col} controlId="formGridSkillLevel">
                <Form.Control
                  required
                  as="select"
                  name="skillLevel"
                  value={skillLevel}
                  onChange={this.handleChange}>
                  <option>Skill Level...</option>
                  <option>INTERNSHIP</option>
                  <option>ENTRYLEVEL</option>
                  <option>ASSOCIATE</option>
                  <option>SENIOR</option>
                  <option>DIRECTOR</option>
                  <option>EXECUTIVE</option>
                </Form.Control>
              </Form.Group>
            </Form.Row>
          </Form>

        </div>
    </div>
    );
  }
}
export default Postings
