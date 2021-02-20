import React from 'react';
import { useSelector, useDispatch } from 'react-redux';

import classes from './LoginButton.module.scss';

import * as actions from '../../../../store/actions';

const LoginButton = () => {
    const dispatch = useDispatch();
    const loggedIn = useSelector(state => state.app.username !== null);
    const buttonText = loggedIn ? 'Sign out' : 'Sign in';

    const onButtonClicked = loggedIn
        ? () => dispatch(actions.clearUsername())
        : () => dispatch(actions.setUsername('admin'));

    return (
        <div className={classes.LoginButton}>
            <button onClick={onButtonClicked}>{buttonText}</button>
        </div>
    );
};

export default LoginButton;
