import React, { useRef } from 'react';
import { useDispatch } from 'react-redux';
import PropTypes from 'prop-types';

import { Form, formInputTypes, validators } from '../../../UI';
import { useAutofocus } from '../../../../hooks';
import * as actions from '../../../../store/actions';

const NewWordDialog = props => {
    const { close, dictionaryId } = props;

    const dispatch = useDispatch();

    const wordGroup = {
        key: 'word',
        label: 'New Word',
    };

    const mainFormRef = useRef();
    const mainFormField = {
        key: 'mainForm',
        label: 'Main Form',
        type: formInputTypes.TEXT,
        defaultValue: '',
        groupKey: wordGroup.key,
        forwardRef: mainFormRef,
        validation: [{
            validate: validators.notEmpty(),
            failureMessage: 'Word main form cannot be empty',
        }],
    };

    useAutofocus(mainFormRef);

    const onConfirm = () => {
        const mainForm = mainFormRef && mainFormRef.current.value.trim();
        if (mainForm)
            dispatch(actions.createWord(dictionaryId, mainForm));
    };

    return <Form
        fields={[mainFormField]}
        groups={[wordGroup]}
        canceled={close}
        confirmed={onConfirm}
    />
};

NewWordDialog.propTypes = {
    close: PropTypes.func.isRequired,
    dictionaryId: PropTypes.string.isRequired,
};

export default NewWordDialog;
