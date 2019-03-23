import React from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter as Router, Route} from "react-router-dom";

import Apply from './views/Apply.jsx';
import Dashboard from './views/Dashboard';
import Description from './views/Description';
import Listings from './views/Listings';
import Login from './views/Login/Login';
import Post from './views/Post';
import Signup from './views/Signup/Signup';
import Splash from './views/Splash/Splash';

import './index.css';
import * as serviceWorker from './serviceWorker';

const routing = (
  <Router>
    <Route exact path="/" component={Splash} />
    <Route path="/login" component={Login} />
    <Route path="/signup" component={Signup} />
    <Route exact path="/listings" component={Listings} />
    <Route path="/listings/description" component={Description} />
    <Route path="/listings/apply" component={Apply} />
    <Route exact path="/dashboard" component={Dashboard} />
    <Route path="/dashboard/post" component={Post} />
  </Router>
)

ReactDOM.render(routing, document.getElementById('root'));

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
