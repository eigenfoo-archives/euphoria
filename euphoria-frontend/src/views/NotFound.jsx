import React, { Component } from 'react';
import {Image} from "react-bootstrap";
import * as globals from "../globals.js";

class NotFound extends Component {
  render() {
    return(
      <div>
        <div className="navbar">
          <div className="logo">
              <Image
                src={require('../images/Logo.png')}
                fluid
                onClick={() => globals.handleRedirect(this.props, "/")}
              />
          </div>
        </div>

        <div className="floating-container centered-container">
          <h1>
            404: NOT FOUND
          </h1>
        </div>
      </div>
    );
}
}
export default NotFound
