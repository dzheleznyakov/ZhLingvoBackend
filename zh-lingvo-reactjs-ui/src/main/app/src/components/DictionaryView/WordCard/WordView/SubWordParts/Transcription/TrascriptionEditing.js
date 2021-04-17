import React from 'react';
import { useDispatch } from 'react-redux';
import PropTypes from 'prop-types';

import Input from '../Input';
import * as actions from '../../../../../../store/actions';

const TranscriptionEditing = props => {
    const { children, parentPath } = props;
    const path = [...parentPath, 'transcription'];
    const dispatch = useDispatch();

    return <Input
        label="Transcription"
        defaultValue={children}
        submit={event => dispatch(actions.updateWordElement(path, event.target.value))}
    />;
};

TranscriptionEditing.propTypes = {
    children: PropTypes.string,
    parentPath: PropTypes.arrayOf(PropTypes.string).isRequired,
};

TranscriptionEditing.defaultProps = {
    children: '',
};

export default TranscriptionEditing;
