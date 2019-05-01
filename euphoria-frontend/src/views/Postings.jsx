import React, { Component } from "react";
import {Image, Button, Container, Row, Col, Form} from "react-bootstrap";
import * as globals from "../globals.js";

import Navbar from "./Navbar";

class Postings extends Component {
  constructor(props, context) {
    super(props);

    this.state = {
      postingsData: [],
      companyData: {},
      location: "...",
      industry: "...",
      skillLevel: "..."
    };

    this.handleChange = this.handleChange.bind(this);
    this.handleGet = this.handleGet.bind(this);

    this.posting = this.posting.bind(this);
  }

  componentDidMount() {
    const url = globals.baseUrl + "/api/posting/random";
    globals.verifyUser(this.props.cookies, this.handleGet(url));

    return;
  }

  handleChange(event) {
    let url = globals.baseUrl + "/api/posting/";

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

      globals.verifyUser(this.props.cookies, this.handleGet(url));
    });

    return;
  }

  handleGet(url, companyId) {
    fetch(url)
      .then(response => response.json())
      .then(data => {
        if(companyId === undefined){
          this.setState({postingsData: data});

          data.forEach(applicationData => {
            const companyURL = globals.baseUrl + "/api/company/" + applicationData.companyId;

            this.handleGet(companyURL, applicationData.companyId);
          });
        }
        else{
          var companyData = this.state.companyData;
          companyData[companyId] = data;
          this.setState({companyData});
        }
      })
      .catch(err => {
      });

    return;
  }

  posting(props) {
    const postingData = props.postingData;
    const companyData = this.state.companyData[postingData.companyId];
    if(companyData === undefined){
      return null;
    }
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
                {postingData.location} - {companyData.name}
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
            <Button variant="info" size="lg" block onClick={() => globals.handleRedirect(this.props, "/postings/apply/" + postingData.postingId)}>
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
      companyData,
      location,
      industry,
      skillLevel,
    } = this.state;
    //console.log(companyData);
    if(Object.keys(companyData).length === 0){
      return(
        <div>
          <Navbar {...this.props}/>
        </div>
      );
    }
    else {
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
}
export default Postings
