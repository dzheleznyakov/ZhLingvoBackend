import React from 'react';
import { useDispatch } from 'react-redux';
import PropTypes from 'prop-types';

import classes from '../Editing.module.scss';

import * as actions from '../../../../../../store/actions';

const TranscriptionEditing = props => {
    const { children, parentPath } = props;
    const path = parentPath.concat(['transcription']);
    const dispatch = useDispatch();

    const onBlur = event => dispatch(actions.updateWordElement(path, event.target.value));

    return (
        <div>
            <label className={classes.Label}>Transcription:</label>
            <input
                className={classes.Input}
                type="text"
                defaultValue={children}
                onBlur={onBlur}
            />
        </div>
    );
};

TranscriptionEditing.propTypes = {
    children: PropTypes.string,
    parentPath: PropTypes.arrayOf(PropTypes.string).isRequired,
};

TranscriptionEditing.defaultProps = {
    children: '',
};

export default TranscriptionEditing;
