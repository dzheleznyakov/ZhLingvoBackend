import React, { useRef } from 'react';
import PropTypes from 'prop-types';
import { useDispatch, useSelector } from 'react-redux';

import * as actions from '../../../../store/actions';
import * as selectors from '../../../../store/selectors';
import { Form, formInputTypes, validators } from '../../../UI';
import { useAutofocus } from '../../../../hooks';

const TranscriptionEditDialog = props => {
    const { editing } = props;
    const path = useSelector(selectors.quizRecordEditPathSelector);
    const transcription = useSelector(
        selectors.quizRecordStringPropertyToUpdateSelectorFactory(path));
    const dispatch = useDispatch();

    const transcriptionGroup = {
        key: 'transcriptionGroup',
        label: editing ? 'Update Transcription' : 'New Transcription',
    };

    const transcriptionRef = useRef();
    const transcriptionField = {
        key: 'transcriptionField',
        label: 'Transcription',
        type: formInputTypes.TEXT,
        defaultValue: transcription,
        groupKey: transcriptionGroup.key,
        forwardRef: transcriptionRef,
        validation: [{
            validate: validators.notEmpty(),
            failureMessage: 'Transcription cannot be empty',
        }],
    };

    useAutofocus(transcriptionRef);

    const onCanceled = () => 
        dispatch(actions.shouldShowQuizRecordEditModal(false));
    const onConfirmed = () => {
        if (transcriptionRef.current) {
            const { value } = transcriptionRef.current;
            dispatch(actions.updateQuizRecordElement(path, value));
        } 
    }

    return (
        <Form
            fields={[transcriptionField]}
            groups={[transcriptionGroup]}
            canceled={onCanceled}
            confirmed={onConfirmed}
        />
    );
};

TranscriptionEditDialog.propTypes = {
    editing: PropTypes.bool,
};

TranscriptionEditDialog.defaultProps = {
    editing: false,
};

export default TranscriptionEditDialog;
