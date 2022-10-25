import React from 'react';
import PropTypes from 'prop-types';

import classes from './Greeting.module.scss';

const Greeting = props => {
    const { title, subtitle } = props;

    return (
    <div className={classes.Titles}>
        <h1>{title}</h1>
        <h2>{subtitle}</h2>
    </div>);
};

Greeting.propTypes = {
    title: PropTypes.string.isRequired,
    subtitle: PropTypes.string.isRequired,
};

export default Greeting;
