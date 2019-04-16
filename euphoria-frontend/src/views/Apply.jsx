import React from 'react'
import {Image, Button, Container, Row, Col} from "react-bootstrap";


class Apply extends React.Component {

  constructor(props, context) {
    super(props);

    this.state = {
      listing_data: [],
    };

    this.handleRedirect = this.handleRedirect.bind(this);
    this.handleGet = this.handleGet.bind(this);
  }

  componentDidMount() {
    this.handleGet();
  }

  handleRedirect(path) {
    this.props.history.push(path);
  }

  handleGet(props) {
    let url = "http://localhost:8080/api/posting/" + this.props.match.params.postingId;

    fetch(url)
    .then(response => {
      return response.json()
    })
    .then(data => {
      // Work with JSON data here
      this.setState({listing_data: data});
    })
    .catch(err => {
      // Do something for an error here
    })

    return;
  }

  listing(props) {
    const listing_data = props.listing_data;
    return(
      <div className="floating-container centered-container" style={{width:"900px"}}>
        <Container fluid>
          <Row>
            <Col sm={9}>
              <h1>
                {listing_data.jobTitle}
              </h1>
            </Col>
            <Col sm={3}>
              <Button variant="info" size="lg">Resume</Button>
            </Col>
          </Row>
          <Row>
            <Col sm={9}>
              <p style={{fontSize:"20px", color:"#AAA"}}>
                {listing_data.location}
              </p>
            </Col>
            <Col sm={3}>
              <Button variant="info" size="lg">Cover Letter</Button>
            </Col>
          </Row>
          <Row>
            <Col>
              <p style={{fontSize:"15px", color:"#AAA"}}>
                {listing_data.industry}
              </p>
            </Col>
            <Col>
              <Image
                src={require("../images/" + listing_data.skillLevel + ".png")}
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
                {listing_data.description}
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
    const listing_data = this.state.listing_data;

    console.log(listing_data);
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


        {listing_data.map(listing_data => (
          <this.listing listing_data={listing_data} />
        ))}

    </div>
    );
  }
}
export default Apply
