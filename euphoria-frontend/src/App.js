import React, { Component } from 'react';
import { Route, Switch, Redirect, withRouter } from "react-router-dom";
import { withCookies } from 'react-cookie';

import Splash from './views/Splash';
import Signup from './views/Signup';
import Signin from './views/Signin';
import Dashboard from './views/Dashboard';
import Post from './views/Post';
import Editpost from './views/Editpost';
import Applications from './views/Applications';
import Postings from './views/Postings';
import Apply from './views/Apply.jsx';
import NotFound from './views/NotFound';

class App extends Component {
  constructor(props, context) {
    super(props);

    this.handleRedirect = this.handleRedirect.bind(this);
  }

  componentDidMount(){
    switch(this.props.cookies.get("isUser")){
      case "true":
        this.handleRedirect("/postings")
        break;
      case "false":
        this.handleRedirect("/dashboard")
        break;
      default:
        break;
    }
  }

  handleRedirect(path) {
    this.props.history.push(path);
  }

  render() {

    return (
      <div>
        <Switch>
          <Route exact path="/" component={Splash} />;
          <Route path="/signin" render={(props) => (<Signin cookies={this.props.cookies} {...props} />)} />
          <Route path="/signup" component={Signup} />
          <Route exact path="/postings" render={(props) => (<Postings cookies={this.props.cookies} {...props} />)} />
          <Route path="/postings/apply/:postingId" render={(props) => (<Apply cookies={this.props.cookies} {...props} />)} />
          <Route exact path="/dashboard" render={(props) => (<Dashboard cookies={this.props.cookies} {...props} />)} />
          <Route exact path="/dashboard/post" render={(props) => (<Post cookies={this.props.cookies} {...props} />)} />
          <Route path="/dashboard/post/edit/:postingId" render={(props) => (<Editpost cookies={this.props.cookies} {...props} />)} />
          <Route path="/dashboard/applications/:postingId" render={(props) => (<Applications cookies={this.props.cookies} {...props} />)} />
          <Route path="/404" component={NotFound} />
          <Redirect to="/404" />
        </Switch>
      </div>
    );
  }
}

export default withRouter(withCookies(App));
