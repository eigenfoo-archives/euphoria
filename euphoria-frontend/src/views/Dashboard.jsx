import React from 'react'
import {Image, Button, ButtonGroup, Container, Row, Col} from "react-bootstrap";

class Dashboard extends React.Component {
  constructor(props, context) {
    super(props);

    this.state = {
      company_postings_data: [],
    };

    this.handleRedirect = this.handleRedirect.bind(this);
    this.handleGet = this.handleGet.bind(this);
    this.handleDelete = this.handleDelete.bind(this);

    this.posting = this.posting.bind(this);
  }

  componentDidMount() {
    const url = "http://localhost:8080/api/posting/random"; //FIXME
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
      // Work with JSON data here
      this.setState({company_postings_data: data});
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
      });

    window.location.reload();
  }

  posting(props) {
    const company_posting_data = props.company_posting_data;

    return(
      <div className="floating-container posting-container-scrolling" style={{width:"600px"}}>
        <Container>
          <Row>
            <Col>
              <h1>
                {company_posting_data.jobTitle}
              </h1>
            </Col>
          </Row>
          <Row>
            <Col>
              <p style={{fontSize:"20px", color:"#AAA"}}>
                {company_posting_data.location}
              </p>
            </Col>
          </Row>
          <Row>
            <Col>
              <p style={{fontSize:"15px", color:"#AAA"}}>
                {company_posting_data.industry}
              </p>
            </Col>
            <Col>
              <Image
                src={require("../images/" + company_posting_data.skillLevel + ".png")}
                style={{height:"20px"}}
              />
            </Col>
          </Row>
          <hr/>
          <br/>
          <Row>
            <Col>
              <p>
                {company_posting_data.description}
              </p>
            </Col>
          </Row>
          <br/>
          <Row>
            <Col sm={8}>
              <Button variant="info" size="lg" block onClick={() => this.handleRedirect("/dashboard/applications/" + company_posting_data.postingId)}>
                View Applications
              </Button>
            </Col>
            <Col sm={4}>
              <ButtonGroup>
                <Button variant="secondary" size="lg">Edit</Button>
                <Button variant="danger" size="lg" onClick={() => this.handleDelete(company_posting_data.postingId)}>Delete</Button>
              </ButtonGroup>
            </Col>
          </Row>
        </Container>
      </div>
    );
  }

  render() {
    const {
      company_postings_data,
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
          {company_postings_data.map(company_posting_data => (
            <this.posting key={company_posting_data.postingId} company_posting_data={company_posting_data} />
          ))}
        </div>
    </div>
    );
  }
}
export default Dashboard
