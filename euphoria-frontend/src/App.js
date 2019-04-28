import React, { Component } from "react";
import { Route, Switch, Redirect, withRouter } from "react-router-dom";
import { withCookies } from "react-cookie";

import Splash from "./views/Splash";
import Signup from "./views/Signup";
import Signin from "./views/Signin";
import Dashboard from "./views/Dashboard";
import Post from "./views/Post";
import Editpost from "./views/Editpost";
import Applications from "./views/Applications";
import Postings from "./views/Postings";
import Apply from "./views/Apply.jsx";
import NotFound from "./views/NotFound";

class App extends Component {
  render() {
    switch(this.props.cookies.get("isUser")){
      case "true":

        return (
          <div>
            <Switch>
              <Route exact path="/postings" render={(props) => (<Postings cookies={this.props.cookies} {...props} />)} />
              <Route path="/postings/apply/:postingId" render={(props) => (<Apply cookies={this.props.cookies} {...props} />)} />
              <Redirect exact from="/" to="/postings" />
              <Route path="/404" component={NotFound} />
              <Redirect to="/404" />
            </Switch>
          </div>
        );

      case "false":
        return (
          <div>
            <Switch>
              <Route exact path="/dashboard" render={(props) => (<Dashboard cookies={this.props.cookies} {...props} />)} />
              <Route exact path="/dashboard/post" render={(props) => (<Post cookies={this.props.cookies} {...props} />)} />
              <Route path="/dashboard/post/edit/:postingId" render={(props) => (<Editpost cookies={this.props.cookies} {...props} />)} />
              <Route path="/dashboard/applications/:postingId" render={(props) => (<Applications cookies={this.props.cookies} {...props} />)} />
              <Redirect exact from="/" to="/dashboard" />
              <Route path="/404" component={NotFound} />
              <Redirect to="/404" />
            </Switch>
          </div>
        );

      default:
        return (
          <div>
            <Switch>
              <Route exact path="/" component={Splash} />;
              <Route path="/signin" render={(props) => (<Signin cookies={this.props.cookies} {...props} />)} />
              <Route path="/signup" component={Signup} />
              <Redirect from="/dashboard" to="/signin" />
              <Redirect from="/postings" to="/signin" />
              <Route path="/404" component={NotFound} />
              <Redirect to="/404" />
            </Switch>
          </div>
        );
    }

  }
}

export default withRouter(withCookies(App));
