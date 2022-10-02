import React, { Fragment, useState, useRef } from 'react';
import { useSelector, useDispatch } from 'react-redux';

import classes from './AuthenticationPanel.module.scss';

import * as actions from '../../../store/actions';
import ErrorContainer from '../../UI/Error/ErrorContainer';
import { useAutofocus } from '../../../hooks';

const AuthentificationPanel = () => {
    const [loginAsExistingUser, setLoginAsExistingUser] = useState(true);
    const dispatch = useDispatch();
    const usernameInputRef = useRef();
    const error = useSelector(store => store.auth.error);

    useAutofocus(usernameInputRef);

    const loginButtonText = loginAsExistingUser ? 'Sign In' : 'Sign Up';
    const loginButtonHandler = loginAsExistingUser
        ? () => dispatch(actions.signIn(usernameInputRef.current.value))
        : () => dispatch(actions.signUp(usernameInputRef.current.value));
    const loginButton = (
        <button className={classes.LoginButton} type="button"
            onClick={loginButtonHandler}>
            {loginButtonText}
        </button>
    );

    const usernameInput = (
        <input 
            className={classes.Input}
            type="text" 
            placeholder="Enter your username"
            ref={usernameInputRef}
            onKeyUp={(event) => {
                event.preventDefault();
                if (event.code === 'Enter')
                    loginButtonHandler();
            }}
        />
    );

    const detailsText = loginAsExistingUser ? 'Not registered yet?' : 'Already registered?';
    const detailsButtonText = loginAsExistingUser ? 'Sign up' : 'Sign in';
    const detailsButtonHandler = () => setLoginAsExistingUser(!loginAsExistingUser);
    const details = (
        <div className={classes.Details}>
            {detailsText}
            <button onClick={detailsButtonHandler}>{detailsButtonText}</button>
        </div>
    );

    const errorContainer = error ? (
        <ErrorContainer className={classes.Error} message={error.response.data.message} />
    ) : null;

    return (
        <Fragment>
            {usernameInput}
            {loginButton}
            {details}
            {errorContainer}
        </Fragment>
    );
};

export default AuthentificationPanel;
