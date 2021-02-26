import React, { useState, useRef, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Redirect } from 'react-router-dom';

import classes from './Authentication.module.scss';

import * as actions from '../../store/actions';
import ErrorContainer from '../UI/Error/ErrorContainer';
import AuthenticationPanel from './AuthentificationPanel/AuthenticationPanel';

const Authentication = () => {
    const loggedIn = useSelector(store => store.auth.username !== null);
    return loggedIn
        ? <Redirect to="/" />
        : (
            <div className={classes.Authentication}>
                <AuthenticationPanel />
            </div>
        );
};

export default Authentication;
