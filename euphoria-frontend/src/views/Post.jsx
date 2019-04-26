import React, { Component } from 'react';
import {Image, Form, Button, Col} from "react-bootstrap";
import * as globalConsts from "../globals.js";

class Post extends Component {

  constructor(props, context) {
    super(props);

    this.state = {
          jobTitle: "",
          description: "",
          location: "",
          industry: "",
          skillLevel: "",
    };

    this.handleChange = this.handleChange.bind(this);
    this.handleRedirect = this.handleRedirect.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  handleChange(event) {
    this.setState({[event.target.name]: event.target.value});
  }

  handleRedirect(path) {
    this.props.history.push(path);
  }

  handleSubmit(event) {
    event.preventDefault(); //prevent redirect with form in url

    const form = event.currentTarget;
    let url = globalConsts.baseUrl + "/api/posting";

    if (form.checkValidity() === false) {
      event.preventDefault();
      event.stopPropagation();
    }

    const companyId = this.props.cookies.get("id");

    const {
      jobTitle,
      description,
      location,
      industry,
      skillLevel
    } = this.state;

    let data = {
      companyId,
      jobTitle,
      description,
      location,
      industry,
      skillLevel
    };

    fetch(url, {
        method: "POST",
        body: JSON.stringify(data)
      })
      .then(response => response.json())
      .then(data => {
        if(data !== "undefined" && data.length === 0){
          alert("Post Created!")
          this.handleRedirect("/postings")
        }
      })
      .catch(err => {
      })
  }

  render() {
    const {
      jobTitle,
      description,
      location,
      industry,
      skillLevel
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

        <div className="floating-container centered-container" style={{width:"600px"}}>
          <h1>Create Posting</h1>
          <hr></hr>
          <Form onSubmit={event => this.handleSubmit(event)}>
            <Form.Row>
              <Form.Group as={Col} controlId="formGridJobTitle">
                <Form.Label>Job Title</Form.Label>
                <Form.Control
                  required
                  type="jobTitle"
                  placeholder="Job Title"
                  name="jobTitle"
                  value={jobTitle}
                  maxLength="24"
                  onChange={this.handleChange}/>
              </Form.Group>

              <Form.Group as={Col} controlId="formGridLocation">
                <Form.Label>Location</Form.Label>
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
            </Form.Row>

            <Form.Row>
              <Form.Group as={Col} controlId="formGridIndustry">
                <Form.Label>Industry</Form.Label>
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
                <Form.Label>Skill Level</Form.Label>
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

            <Form.Group controlId="formGridDescription">
              <Form.Label>Description</Form.Label>
              <Form.Control
                required
                as="textarea"
                name="description"
                value={description}
                maxLength="5000"
                placeholder="Descrption..."
                rows="5"
                style={{resize:"none"}}
                onChange={this.handleChange}/>
            </Form.Group>

            <Button variant="info" type="submit">
              Submit
            </Button>
          </Form>
        </div>
      </div>
    );
  }
}
export default Post
