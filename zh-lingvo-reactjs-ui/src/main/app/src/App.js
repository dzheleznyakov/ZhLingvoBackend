import React from 'react';
import { Switch, Redirect, Route, withRouter } from 'react-router-dom';

import './App.scss';

import Layout from './hoc/Layout/Layout';

const App = () => {
  return (
    <Layout>
      Content
    </Layout>
  );
};

export default App;
