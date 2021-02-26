import React from 'react';
import { Switch, Redirect, Route, withRouter } from 'react-router-dom';

import Layout from './hoc/Layout/Layout';
import asyncComponent from './hoc/asyncComponent/asyncComponent';

const asyncAuthentication = asyncComponent(() => {
  return import('./components/Authentication/Authentication');
});

const asyncHome = asyncComponent(() => import('./components/Control/Navigation/LoginButton/LoginButton'));

const App = () => {
  const routes = (
    <Switch>
      <Route exact path="/auth" component={asyncAuthentication} />
      <Route exact path="/" component={asyncHome} />
      <Redirect to="/" />
    </Switch>
  );

  return (
    <Layout>
      {routes}
    </Layout>
  );
};

export default withRouter(App);
