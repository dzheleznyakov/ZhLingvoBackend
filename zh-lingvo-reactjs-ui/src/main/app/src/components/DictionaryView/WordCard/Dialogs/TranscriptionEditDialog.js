import React, { useRef } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import PropTypes from 'prop-types';

import { Form, formInputTypes, validators } from '../../../UI';
import { useAutofocus } from '../../../../hooks';
import * as actions from '../../../../store/actions';
import * as selectors from '../../../../store/selectors';

const TranscriptionEditDialog = props => {
    const { editting } = props;
    const wordEditPath = useSelector(selectors.wordEditPathSelector);
    const transcription = useSelector(selectors.stringPropertyToUpdateSelectorFactory(wordEditPath));
    const dispatch = useDispatch();

    const transcriptionGroup = {
        key: 'transcriptionGroup',
        label: editting ? 'Update Transcription' : 'New Transcription',
    };

    const transcriptionRef = useRef();
    const transcriptionField = {
        key: 'transcriptionField',
        label: 'Transcription',
        type: formInputTypes.TEXT,
        defaultValue: transcription || '',
        groupKey: transcriptionGroup.key,
        forwardRef: transcriptionRef,
        validation: [{
            validate: validators.notEmpty(),
            failureMessage: 'Transcription cannot be empty',
        }],
    };

    useAutofocus(transcriptionRef);

    return <Form 
        fields={[transcriptionField]}
        groups={[transcriptionGroup]}
        canceled={() => dispatch(actions.shouldShowWordEditModal(false))}
        confirmed={() => dispatch(actions.updateWordElement(wordEditPath, transcriptionRef.current.value))}
    />;
};

TranscriptionEditDialog.propTypes = {
    editting: PropTypes.bool,
};

export default TranscriptionEditDialog;
