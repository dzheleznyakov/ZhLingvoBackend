import React, { useEffect, useRef, useState } from 'react';
import { useDispatch } from 'react-redux';
import PropTypes from 'prop-types';

import classes from './NewWordDialog.module.scss';

import { Dialog, Error } from '../../../UI';
import { useAutofocus } from '../../../../hooks';
import * as actions from '../../../../store/actions';
import { useParams } from 'react-router';

const NewWordDialog = props => {
    const { close } = props;

    const { id: dictionaryId } = useParams();
    const [errorMessages, setErrorMessages] = useState({});
    const mainFormRef = useRef();
    const dispatch = useDispatch();

    useAutofocus(mainFormRef);

    const onConfirm = () => {
        const noErrorsExist = Object.keys(errorMessages).length === 0;
        const mainForm = mainFormRef && mainFormRef.current.value.trim();
        if (noErrorsExist && mainForm) {
            dispatch(actions.createWord(dictionaryId, mainForm));
        }
    };

    const verify = () => {
        const updatedErrors = { ...errorMessages };
        const mainForm = mainFormRef && mainFormRef.current.value;
        if (!mainForm || !mainForm.trim())
            updatedErrors.mainForm = 'A word should have main form';
        else
            updatedErrors.mainForm && delete updatedErrors.mainForm;
        setErrorMessages(updatedErrors);
    };

    useEffect(() => {
        mainFormRef && verify();
    }, [mainFormRef]);

    const errors = Object.keys(errorMessages)
        .map(key => <Error key={`err-${key}`} message={errorMessages[key]} />)

    return (
        <Dialog close={close} confirmed={onConfirm}>
            <fieldset className={classes.NewWordDialog}>
                <legend>New Word</legend>
                <div className={classes.FieldsWrapper}>
                    <label>Main Form:</label>
                    <input
                        type="text"
                        ref={mainFormRef}
                        onKeyUp={verify}
                    />
                </div>
            </fieldset>
            {errors}
        </Dialog>
    );
};

NewWordDialog.propTypes = {
    close: PropTypes.func.isRequired,
};

export default NewWordDialog;
