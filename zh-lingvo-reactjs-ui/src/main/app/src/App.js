import React, { useEffect } from 'react';
import { Switch, Redirect, Route, withRouter } from 'react-router-dom';
import { useDispatch } from 'react-redux';

import Layout from './hoc/Layout/Layout';
import * as actions from './store/actions';
import asyncComponent from './hoc/asyncComponent/asyncComponent';

const asyncAuthentication = asyncComponent(() => {
  return import('./components/Authentication/Authentication');
});

const asyncHome = asyncComponent(() => import('./components/Home/Home'));

const App = () => {
  const dispath = useDispatch();

  useEffect(() => {
    dispath(actions.autoSignIn());
  }, [dispath]);

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
