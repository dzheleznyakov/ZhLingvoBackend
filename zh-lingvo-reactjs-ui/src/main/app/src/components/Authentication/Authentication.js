import React from 'react';

import classes from './Authentication.module.scss';

import { useBreadcrumbs } from '../../hooks';
import AuthenticationPanel from './AuthentificationPanel/AuthenticationPanel';
import { BREADCRUMBS_TYPES } from '../../utils/breadcrumbs';

const BREADCRUMBS = [{ type: BREADCRUMBS_TYPES.TEXT, text: 'Log in' }]

const Authentication = () => {
    useBreadcrumbs(...BREADCRUMBS);

    return (
        <div className={classes.Authentication}>
            <AuthenticationPanel />
        </div>
    );
};

export default Authentication;
