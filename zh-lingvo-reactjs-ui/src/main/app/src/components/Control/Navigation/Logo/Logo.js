import React from 'react';
import PropTypes from 'prop-types';
import { Link } from 'react-router-dom';

import classes from './Logo.module.scss';

const Logo = ({ postClicked }) => (
    <Link 
        to="/"
        onClick={() => postClicked && postClicked()}
    >
        <div className={classes.Logo} />
    </Link>
);

Logo.propTypes = {
    postClicked: PropTypes.func,
};

export default Logo;
