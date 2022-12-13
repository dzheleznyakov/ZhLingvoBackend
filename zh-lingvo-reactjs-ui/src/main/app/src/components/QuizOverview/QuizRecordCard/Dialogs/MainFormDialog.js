import React, { useRef } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useAutofocus } from '../../../../hooks';

import * as selectors from '../../../../store/selectors';
import * as actions from '../../../../store/actions';
import { Form, formInputTypes, validators } from '../../../UI';

const MainFormDialog = () => {
    const mainForm = useSelector(selectors.quizRecordMainFormToUpdateSelector);
    const dispatch = useDispatch();

    const mainFormGroup = {
        key: 'mainFormGroup',
        label: 'Update Word Main Form',
    };

    const mainFormRef = useRef();
    const mainFormField = {
        key: 'mainFormField',
        label: 'Main Form',
        type: formInputTypes.TEXT,
        defaultValue: mainForm,
        groupKey: mainFormGroup.key,
        forwardRef: mainFormRef,
        validation: [{
            validate: validators.notEmpty(),
            failureMessage: 'Word main form cannot be empty',
        }],
    };

    useAutofocus(mainFormRef);

    return <Form
        fields={[mainFormField]}
        groups={[mainFormGroup]}
        canceled={() => dispatch(actions.shouldShowQuizRecordEditModal(false))}
        confirmed={() => dispatch(actions.updateQuizRecordMainForm(mainFormRef.current.value))}
    />;
};

export default MainFormDialog;
