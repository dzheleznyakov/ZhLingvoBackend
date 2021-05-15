import React from 'react';
import PropTypes from 'prop-types';

import classes from './Transcription.module.scss';

const EditableTranscription = props => {
    const { children: transcription } = props;

return transcription && (
        <span className={classes.Transcription}>
            [{transcription}]
        </span>
    );
};

EditableTranscription.propTypes = {
    children: PropTypes.string,
};

export default EditableTranscription;
