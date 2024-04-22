import React, { useRef } from 'react';
import { useDispatch, useSelector } from 'react-redux';

import { Form, formInputTypes, validators } from '../../../UI';
import { useAutofocus } from '../../../../hooks';
import * as actions from '../../../../store/actions';
import * as selectors from '../../../../store/selectors';

const MainFormDialog = () => {
    const mainForm = useSelector(selectors.mainFormToUpdateSelector);
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
        autocomplete: false,
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
        canceled={() => dispatch(actions.shouldShowWordEditModal(false))}
        confirmed={() => dispatch(actions.updateWordMainForm(mainFormRef.current.value))}
    />;
};

export default MainFormDialog;
