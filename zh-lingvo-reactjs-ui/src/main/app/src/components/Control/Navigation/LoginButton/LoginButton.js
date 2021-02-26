import React from 'react';
import PropTypes from 'prop-types';
import { useSelector, useDispatch } from 'react-redux';
import { useHistory } from 'react-router-dom';

import classes from './LoginButton.module.scss';

import * as actions from '../../../../store/actions';

const LoginButton = props => {
    const { postClicked } = props;
    const dispatch = useDispatch();
    const history = useHistory();

    const loggedIn = useSelector(state => state.auth.username !== null);
    const buttonText = loggedIn ? 'Log out' : 'Log in';

    const loginAction = loggedIn
        ? () => dispatch(actions.clearUsername())
        : () => history.push('/auth');

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
