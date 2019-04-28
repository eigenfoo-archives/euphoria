import React, { Component } from "react";

import Navbar from "./Navbar";

class NotFound extends Component {
  render() {
    return(
      <div>
        <Navbar {...this.props}/>

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
