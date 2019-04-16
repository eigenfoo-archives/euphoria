import React from 'react'
import {Image, Button, Container, Row, Col} from "react-bootstrap";

class Listings extends React.Component {

  constructor(props, context) {
    super(props);

    this.state = {
      listings_data: []
    };

    this.handleRedirect = this.handleRedirect.bind(this);
    this.handleGet = this.handleGet.bind(this);

    this.listing = this.listing.bind(this);
  }

  componentDidMount() {
    this.handleGet();
  }

  handleRedirect(path) {
    this.props.history.push(path);
  }

  handleGet(props) {

    let url = "http://localhost:8080/api/posting/random";

    fetch(url)
    .then(response => {
      return response.json()
    })
    .then(data => {
      // Work with JSON data here
      this.setState({listings_data: data});
    })
    .catch(err => {
      // Do something for an error here
    })

    return;
  }

  listing(props) {
    const listing_data = props.listing_data;

    return(
      <div className="floating-container listing-container-scrolling" style={{width:"600px"}}>
        <Container>
          <Row>
            <Col>
              <h1>
                {listing_data.jobTitle}
              </h1>
            </Col>
          </Row>
          <Row>
            <Col>
              <p style={{fontSize:"20px", color:"#AAA"}}>
                {listing_data.location}
              </p>
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
          <hr/>
          <br/>
          <Row>
            <Col>
              <p>
                {listing_data.description}
              </p>
            </Col>
          </Row>
          <br/>
          <Row>
            <Button variant="info" size="lg" block onClick={() => this.handleRedirect("/listings/apply/" + listing_data.postingId)}>
              Apply
            </Button>
          </Row>
        </Container>
      </div>
    );
  }

  render() {
    const listings_data = this.state.listings_data;
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
          {listings_data.map(listing_data => (
            <this.listing key={listing_data.postingId} listing_data={listing_data} />
          ))}

        </div>
    </div>
    );
  }
}
export default Listings
