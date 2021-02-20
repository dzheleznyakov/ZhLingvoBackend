import React from 'react';

import Navigation from '../Navigation/Navigation';

import classes from './Toolbar.module.scss';

const Toolbar = () => (
    <div className={classes.Toolbar}>
        <Navigation />
    </div>
);

export default Toolbar;
