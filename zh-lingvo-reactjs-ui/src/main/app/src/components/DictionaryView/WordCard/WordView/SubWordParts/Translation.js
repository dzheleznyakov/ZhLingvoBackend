import React from 'react';
import PropTypes from 'prop-types';

import classes from './Translation.module.scss';

import { translationType } from '../wordTypes';

const Translation = props => {
    const { entry } = props;
    const elaboration = entry.elaboration ? (
        <span className={classes.Elaboration}> ({entry.elaboration})</span>
    ) : null;

    return (
        <span className={classes.Translation}>
            <span>{entry.value}</span>
            {elaboration}
        </span>
    );
};

Translation.propTypes = {
    entry: translationType.isRequired,
};

export default Translation;
