import React from 'react';

import Logo from './Navigation/Logo/Logo';
import AppChooser from './Navigation/AppChooser/AppChooser';
import LoginButton from './Navigation/LoginButton/LoginButton';
import VerticalRule from '../../UI/VerticalRule/VerticalRule';
import Navigation from './Navigation/Navigation';

import classes from './Toolbar.module.scss';

const Toolbar = props => {
    return (
        <div className={classes.Toolbar}>
            <Navigation />
        </div>
    );
};

export default Toolbar;
