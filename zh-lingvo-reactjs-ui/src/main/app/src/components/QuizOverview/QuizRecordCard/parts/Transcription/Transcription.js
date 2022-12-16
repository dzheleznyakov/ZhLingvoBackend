import React from 'react';
import PropTypes from 'prop-types';

import classes from './Transcription.module.scss';

const Transcription = props => {
    const { children: transcription } = props;
    return transcription && (
        <div className={classes.Transcription}>
            [{transcription}]
        </div>
    );
};

Transcription.propTypes = {
    children: PropTypes.string,
};

export default Transcription;
