import React, { useState } from 'react';
import PropTypes from 'prop-types';

import classes from './HamburgerButton.module.scss';

const HamburgerButton = props => {
    const { clicked } = props;

    return (
        <button 
            type="button" 
            className={classes.HamburgerButton}
            onClick={clicked}
        >
            <div className={classes.Hamburger} />
        </button>
    );
};

HamburgerButton.propTypes = {
    clicked: PropTypes.func.isRequired,
};

export default HamburgerButton;
