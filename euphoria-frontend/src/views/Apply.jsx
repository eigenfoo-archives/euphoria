import React from 'react'
import {Image, Button, Container, Row, Col} from "react-bootstrap";

class Apply extends React.Component {

  constructor(props, context) {
    super(props);

    this.state = {
      posting_data: [],
    };

    this.handleRedirect = this.handleRedirect.bind(this);
    this.handleGet = this.handleGet.bind(this);
  }

  componentDidMount() {
    let url = "http://localhost:8080/api/posting/" + this.props.match.params.postingId;
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
      this.setState({posting_data: data});
    })
    .catch(err => {
      // Do something for an error here
    })

    return;
  }

  posting(props) {
    const posting_data = props.posting_data;
    return(
      <div className="floating-container centered-container" style={{width:"900px"}}>
        <Container fluid>
          <Row>
            <Col sm={9}>
              <h1>
                {posting_data.jobTitle}
              </h1>
            </Col>
            <Col sm={3}>
              <Button variant="info" size="lg">Resume</Button>
            </Col>
          </Row>
          <Row>
            <Col sm={9}>
              <p style={{fontSize:"20px", color:"#AAA"}}>
                {posting_data.location}
              </p>
            </Col>
            <Col sm={3}>
              <Button variant="info" size="lg">Cover Letter</Button>
            </Col>
          </Row>
          <Row>
            <Col>
              <p style={{fontSize:"15px", color:"#AAA"}}>
                {posting_data.industry}
              </p>
            </Col>
            <Col>
              <Image
                src={require("../images/" + posting_data.skillLevel + ".png")}
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
                {posting_data.description}
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
    const posting_data = this.state.posting_data;

    console.log(posting_data);
    return(
      <div>
        <div className="navbar">
          <div className="logo">
            <Image
              src={require('../images/Logo.png')}
              fluid
              onClick={() => this.handleRedirect("/posting")}
            />
          </div>
        </div>


        {posting_data.map(posting_data => (
          <this.posting posting_data={posting_data} />
        ))}
    </div>

    );
  }
}
export default Apply
