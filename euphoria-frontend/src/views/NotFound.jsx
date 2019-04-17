import React from "react";
import {Image} from "react-bootstrap";

class NotFound extends React.Component {
  constructor(props, context) {
    super(props);

    this.handleRedirect = this.handleRedirect.bind(this);
  }

  handleRedirect(path) {
    this.props.history.push(path);
  }

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
