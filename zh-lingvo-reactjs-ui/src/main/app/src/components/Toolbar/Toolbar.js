import React from 'react';

import Logo from './Logo/Logo';
import AppChooser from './AppChooser/AppChooser';
import LoginButton from './LoginButton/LoginButton';
import VerticalRule from '../UI/VerticalRule/VerticalRule';

import classes from './Toolbar.module.scss';

const Toolbar = props => {
    return (
        <div className={classes.Toolbar}>
            <Logo />
            <VerticalRule />
            <AppChooser />
            <LoginButton />
        </div>
    );
};

export default Toolbar;
