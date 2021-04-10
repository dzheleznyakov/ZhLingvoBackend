import React from 'react';
import PropTypes from 'prop-types';
import { useSelector, useDispatch } from 'react-redux';

import classes from './LoginButton.module.scss';

import * as actions from '../../../../store/actions';
import { loggedInSelector } from '../../../../store/selectors';

const LoginButton = props => {
    const { postClicked } = props;
    const dispatch = useDispatch();

    const loggedIn = useSelector(loggedInSelector);
    const buttonText = loggedIn ? 'Log out' : 'Log in';

    const loginAction = loggedIn
        ? () => dispatch(actions.signOut())
        : () => dispatch(actions.navigateTo('/auth'));

    const onButtonClicked = () => {
        loginAction();
        postClicked && postClicked();
    }

    return (
        <div className={classes.LoginButton}>
            <button onClick={onButtonClicked}>{buttonText}</button>
        </div>
    );
};

LoginButton.propTypes = {
    postClicked: PropTypes.func,
}

export default LoginButton;
