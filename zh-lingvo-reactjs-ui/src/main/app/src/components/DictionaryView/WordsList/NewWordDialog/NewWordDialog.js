import React, { useRef, useState } from 'react';
import { useDispatch } from 'react-redux';
import PropTypes from 'prop-types';

import { Form, formInputTypes, validators } from '../../../UI';
import { useAutofocus } from '../../../../hooks';
import * as actions from '../../../../store/actions';
import { useParams } from 'react-router';

const NewWordDialog = props => {
    const { close } = props;

    const { id: dictionaryId } = useParams();
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
};

export default NewWordDialog;
