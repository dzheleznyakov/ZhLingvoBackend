import { useAppDispatch, useAppSelector } from '../../../store/hooks/reduxHooks';
import { authActions } from '../../../store/reducers/auth';

const AuthenticationPanel = () => {
  const dispatch = useAppDispatch();
  const loggedIn = useAppSelector((s) => s.auth.username) !== null;

  const handleClick = () => {
    console.log(authActions, loggedIn);
    if (loggedIn) dispatch(authActions.signOut());
    else dispatch(authActions.signIn('admi'));
  };
  return <button onClick={handleClick}>{loggedIn ? 'Sign Out' : 'Sign In'}</button>;
};

export default AuthenticationPanel;
