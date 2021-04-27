import React from 'react';
import PropTypes from 'prop-types';

import classes from './Translation.module.scss';

import { translationType } from '../../wordTypes';

const Translation = props => {
    const { entry, postfix } = props;
    const { value: translation, elaboration } = entry;
    const elaborationComp = entry.elaboration ? (
        <span className={classes.Elaboration}> ({elaboration})</span>
    ) : null;

    return (
        <span className={classes.Translation}>
            <span>{translation}</span>
            {elaborationComp}
            {postfix}
        </span>
    );
};

Translation.propTypes = {
    entry: translationType.isRequired,
    postfix: PropTypes.string,
};

export default Translation;
