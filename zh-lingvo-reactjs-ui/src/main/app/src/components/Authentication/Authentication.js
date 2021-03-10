import React from 'react';

import classes from './Authentication.module.scss';

import { useBreadcrumbs } from '../../hooks';
import AuthenticationPanel from './AuthentificationPanel/AuthenticationPanel';

const BREADCRUMBS = ['Log in']

const Authentication = () => {
    useBreadcrumbs(...BREADCRUMBS);

    return (
        <div className={classes.Authentication}>
            <AuthenticationPanel />
        </div>
    );
};

export default Authentication;
