import React, { lazy, Suspense } from 'react';
import { Switch, Redirect, Route, withRouter } from 'react-router-dom';
import { useSelector } from 'react-redux';

import Layout from './hoc/Layout/Layout';
import Spinner from './components/UI/Spinner/Spinner';
import RouterHelper from './components/RouterHelper/RouterHelper';
import * as actions from './store/actions';
import { loggedInSelector } from './store/selectors';
import * as paths from './static/constants/paths';
import { useActionOnMount, useLastVisitedPage } from './hooks';

const Authentication = React.lazy(() => {
  return import('./components/Authentication/Authentication');
});

const Home = lazy(() => {
  return import('./components/Home/Home');
});

const Dictionaries = lazy(() => {
  return import('./components/DictionaryMain/DictionaryMain');
});

const DictionaryView = lazy(() => {
  return import('./components/DictionaryView/DictionaryView');
})

const Tutor = lazy(() => {
  return import('./components/TutorMain/TutorMain');
})

const App = () => {
  const loggedIn = useSelector(loggedInSelector);

  useActionOnMount(actions.autoSignIn())
  useLastVisitedPage();

  const routes = loggedIn ? (
    <Switch>
      <Route exact path={paths.DICTIONARY} render={() => <DictionaryView />} />
      <Route exact path={paths.DICTIONARIES_ROOT} render={() => <Dictionaries />} />
      <Route exact path={paths.TUTOR_ROOT} render={() => <Tutor />} />
      <Route exact path={paths.TUTOR_QUIZ} render={() => <div>Quiz</div>} />
      <Redirect to={paths.DICTIONARIES_ROOT} />
    </Switch>
  ) : (
    <Switch>
      <Route path={paths.ROOT} render={() => <Authentication />} />
      <Route exact path={paths.ROOT} render={() => <Home />} />
      <Redirect to={paths.ROOT} />
    </Switch>
  );

  return (
    <Layout>
      <Suspense fallback={<Spinner />}>{routes}</Suspense>
      <RouterHelper />
    </Layout>
  );
};

export default withRouter(App);
