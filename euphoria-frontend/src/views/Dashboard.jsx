import React from 'react'
import {Image, Button, Container, Row, Col} from "react-bootstrap";

class Dashboard extends React.Component {
  render() {
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
      </div>
    );
  }
}
export default Dashboard
