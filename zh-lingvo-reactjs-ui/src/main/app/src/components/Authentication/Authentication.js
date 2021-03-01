import React, { useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { Redirect } from 'react-router-dom';

import classes from './Authentication.module.scss';

import * as actions from '../../store/actions';
import AuthenticationPanel from './AuthentificationPanel/AuthenticationPanel';

const BREADCRUMBS = ['Log in']

const Authentication = () => {
    const loggedIn = useSelector(store => store.auth.username !== null);
    const dispatch = useDispatch();

    useEffect(() => {
        dispatch(actions.setBreadcrumbs(BREADCRUMBS));
    }, [dispatch]);

    return loggedIn
        ? <Redirect to="/" />
        : (
            <div className={classes.Authentication}>
                <AuthenticationPanel />
            </div>
        );
};

export default Authentication;
