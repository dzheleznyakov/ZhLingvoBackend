import React from 'react';
import { Switch, Redirect, Route, withRouter } from 'react-router-dom';

import './App.css';

import Layout from './hoc/Layout/Layout';
// import asyncComponent from './hoc/asyncComponent/asyncComponent';

const App = () => {
  return (
    <Layout>
      Content
    </Layout>
  );
};

export default App;
