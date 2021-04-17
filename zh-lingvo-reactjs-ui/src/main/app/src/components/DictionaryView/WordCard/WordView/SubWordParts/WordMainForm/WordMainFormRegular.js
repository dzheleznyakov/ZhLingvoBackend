import React, { useEffect, useRef, useState } from 'react';
import PropTypes from 'prop-types';

import classes from './WordMainForm.module.scss';


const WordMainFormRegular = props => {
    const { children } = props;
    return <div className={classes.WordMainForm}>{children}</div>;
};

WordMainFormRegular.propTypes = {
    children: PropTypes.string.isRequired,
};

export default WordMainFormRegular;
