import React, { Component } from 'react';
import { Route, Switch, Redirect } from "react-router-dom";
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
  render() {
    return (
      <div>
        <Switch>
          <Route exact path="/" component={Splash} />
          <Route path="/signin" component={Signin} />
          <Route path="/signup" component={Signup} />
          <Route exact path="/postings" component={Postings} />
          <Route path="/postings/apply/:postingId" component={Apply} />
          <Route exact path="/dashboard" component={Dashboard} />
          <Route exact path="/dashboard/post" component={Post} />
          <Route path="/dashboard/post/edit/:postingId" component={Editpost} />
          <Route path="/dashboard/applications/:postingId" component={Applications} />
          <Route path="/404" component={NotFound} />
          <Redirect to="/404" />
        </Switch>
      </div>
    );
  }
}

export default App;
