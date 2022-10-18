import React, { useRef } from 'react';
import PropTypes from 'prop-types';
import { useParams } from 'react-router';
import { useDispatch, useSelector } from 'react-redux';

import { Form, formInputTypes, validators } from '../../../../UI';
import { useAutofocus } from '../../../../../hooks';
import * as actions from '../../../../../store/actions';
import { posListSelector } from '../../../../../store/selectors';

const NewRecordDialog = props => {
    const { close } = props;
    const { qid: quizId } = useParams();
    const posList = useSelector(posListSelector)
    const dispatch = useDispatch();

    const recordGroup = {
        key: 'record',
        label: 'New Record',
    };

    const mainFormRef = useRef();
    const mainFormField = {
        key: 'mainForm',
        label: 'Main Form',
        type: formInputTypes.TEXT,
        defaultValue: '',
        groupKey: recordGroup.key,
        forwardRef: mainFormRef,
        validation: [{
            validate: validators.notEmpty(),
            failureMessage: 'Quiz record main form cannot be empty',
        }],
    };

    const posRef = useRef();
    const posField = {
        key: 'pos',
        label: 'Part of Speech',
        type: formInputTypes.SELECT,
        values: posList.map(pos => pos.nativeName),
        defaultValue: posList[0].nativeName,
        groupKey: recordGroup.key,
        forwardRef: posRef,
    };

    useAutofocus(mainFormRef);

    const onConfirm = () => {
        const mainForm = mainFormRef && mainFormRef.current.value.trim();
        const posShort = posRef && posList.find(pos => pos.nativeName === posRef.current.value).defaultShortName;
        if (mainForm)
            dispatch(actions.createQuizRecord(quizId, mainForm, posShort));
    };

    return <Form 
        groups={[recordGroup]}
        fields={[mainFormField, posField]}
        canceled={close}
        confirmed={onConfirm}
    />;
};

NewRecordDialog.propTypes = {
    close: PropTypes.func.isRequired,
};

export default NewRecordDialog;
