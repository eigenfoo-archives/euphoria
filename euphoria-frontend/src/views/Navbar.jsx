import React, { Component } from 'react';
import {Image} from "react-bootstrap";
import * as globals from "../globals.js";

class Navbar extends Component {
  render() {
    return(
      <div className="navbar">
        <div className="logo">
          <Image
            src={require('../images/Logo.png')}
            fluid
            onClick={() => globals.handleRedirect(this.props, "/")}
          />
        </div>
      </div>
    );
}
}
export default Navbar
