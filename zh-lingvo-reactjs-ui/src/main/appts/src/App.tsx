import { lazy, Suspense, useMemo } from 'react';
import { createBrowserRouter, Navigate, Outlet, redirect, RouterProvider } from 'react-router-dom';

import Layout from './features/layout/components/Layout';
import Spinner from './features/ui/components/Spinner';

const Authentication = lazy(() => {
  return import('./features/auth/components/AuthenticationPanel');
});

function LayoutRoute() {
  return (
    <Layout>
      <Suspense fallback={<Spinner />}>
        <Outlet />
      </Suspense>
    </Layout>
  );
}

function requireAuth(isAuthed: boolean) {
  if (!isAuthed) throw redirect('/auth');
  return null;
}

function requireGuest(isAuthed: boolean) {
  if (isAuthed) throw redirect('/dictionaries');
  return null;
}

function App() {
  const loggedIn = false;

  const router = useMemo(
    () =>
      createBrowserRouter([
        {
          element: <LayoutRoute />,
          children: [
            {
              loader: () => requireGuest(loggedIn),
              children: [
                { path: '/auth', element: <Authentication /> },
                { path: '*', element: <Navigate to={'/auth'} replace /> },
              ],
            },
            {
              loader: () => requireAuth(loggedIn),
              children: [{ path: '/dictionaries', element: <div>Dictionaries</div> }],
            },
          ],
        },
      ]),
    [loggedIn],
  );

  return <RouterProvider router={router} />;
}

export default App;
