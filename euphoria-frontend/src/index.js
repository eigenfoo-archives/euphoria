import React from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter as Router, Route, Switch, Redirect} from "react-router-dom";
import { CookiesProvider } from 'react-cookie';
import Particles from 'react-particles-js';

import App from './App.js';

import './index.css';
import './views/Styles.css';
import * as serviceWorker from './serviceWorker';

const routing = (
  <CookiesProvider>
    <Router>
      <Particles
        params={require("./images/particles_params.json")}
        style={{
          position: "fixed",
          width: "100%",
          height: "100%"
        }} />
      <App />
    </Router>
  </CookiesProvider>
)

ReactDOM.render(routing, document.getElementById('root'));

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
