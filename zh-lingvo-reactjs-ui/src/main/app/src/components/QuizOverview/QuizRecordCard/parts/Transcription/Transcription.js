import React from 'react';
import PropTypes from 'prop-types';

import classes from './Transcription.module.scss';

const Transcription = props => {
    const { children: transcription } = props;
    return (
        <div className={classes.Transcription}>
            [{transcription}]
        </div>
    );
};

Transcription.propTypes = {
    children: PropTypes.string.isRequired,
};

export default Transcription;
