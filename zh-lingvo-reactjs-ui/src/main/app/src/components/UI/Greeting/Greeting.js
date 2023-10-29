import React from 'react';
import PropTypes from 'prop-types';

import classes from './Greeting.module.scss';

const Greeting = props => {
    const { title, subtitle } = props;

    return (
    <div className={classes.Titles}>
        {title && <h1>{title}</h1>}
        {subtitle && <h2>{subtitle}</h2>}
    </div>);
};

Greeting.propTypes = {
    title: PropTypes.string,
    subtitle: PropTypes.string,
};

export default Greeting;
