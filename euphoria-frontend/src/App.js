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
  }

  render() {

    const props = this.props;

    return (
      <div>
        <Switch>
          <Route exact path="/" component={Splash} />
          <Route path="/signin" render={(props) => (<Signin {...props} />)} />
          <Route path="/signup" component={Signup} />
          <Route exact path="/postings" render={(props) => (<Postings {...props} />)} />
          <Route path="/postings/apply/:postingId" render={(props) => (<Apply {...props} />)} />
          <Route exact path="/dashboard" render={(props) => (<Dashboard {...props} />)} />
          <Route exact path="/dashboard/post" render={(props) => (<Post {...props} />)} />
          <Route path="/dashboard/post/edit/:postingId" render={(props) => (<Editpost {...props} />)} />
          <Route path="/dashboard/applications/:postingId" render={(props) => (<Applications {...props} />)} />
          <Route path="/404" component={NotFound} />
          <Redirect to="/404" />
        </Switch>
      </div>
    );
  }
}

export default withRouter(withCookies(App));
