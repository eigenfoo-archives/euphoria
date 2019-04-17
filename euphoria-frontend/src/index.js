import React from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter as Router, Route, Switch, Redirect} from "react-router-dom";
import { CookiesProvider } from 'react-cookies';
import Particles from 'react-particles-js';

import Splash from './views/Splash';
import Signup from './views/Signup';
import Signin from './views/Signin';
import Dashboard from './views/Dashboard';
import Post from './views/Post';
import Applications from './views/Applications';
import Postings from './views/Postings';
import Apply from './views/Apply.jsx';
import NotFound from './views/NotFound';

import './index.css';
import './views/Styles.css';
import * as serviceWorker from './serviceWorker';

const routing = (
  <Router>
    <Particles
      params={require("./images/particles_params.json")}
      style={{
        position: "fixed",
        width: "100%",
        height: "100%"
      }} />
    <Switch>
      <Route exact path="/" component={Splash} />
      <Route path="/signin" component={Signin} />
      <Route path="/signup" component={Signup} />
      <Route exact path="/postings" component={Postings} />
      <Route path="/postings/apply/:postingId" component={Apply} />
      <Route exact path="/dashboard" component={Dashboard} />
      <Route path="/dashboard/post" component={Post} />
      <Route path="/dashboard/applications/:postingId" component={Applications} />
      <Route path="/404" component={NotFound} />
      <Redirect to="/404" />
    </Switch>
  </Router>
)

ReactDOM.render(routing, document.getElementById('root'));

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
