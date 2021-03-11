import React, { useEffect, useRef, useState } from 'react';
import PropTypes from 'prop-types';
import { useDispatch, useSelector } from 'react-redux';

import classes from './DictionaryForm.module.scss';

import { Dialog, Error } from '../../UI';
import * as actions from '../../../store/actions';
import { languagesSelector } from '../../../store/selectors';
import { useActionOnMount, useAutofocus } from '../../../hooks';

const DictionaryForm = props => {
    const { close, title } = props;
    const [errorMessages, setErrorMessages] = useState({});
    const dispatch = useDispatch();
    const languages = useSelector(languagesSelector);
    const nameInputRef = useRef();
    const languageSelectRef = useRef();

    useActionOnMount(actions.fetchAllLanguages());
    
    const verify = () => {
        const updatedErrors = { ...errorMessages };
        const name = nameInputRef && nameInputRef.current.value;
        if (!name || name.trim().length < 3)
            updatedErrors.name = 'Dictionary name should be of length at least 3';
        else
            updatedErrors.name && delete updatedErrors.name;
        setErrorMessages(updatedErrors);
    }

    useEffect(() => {
        nameInputRef && verify();
    }, [nameInputRef]);

    useAutofocus(nameInputRef);

    const confirmed = () => {
        const name = nameInputRef.current.value;
        const language = languages.find(lang => lang.name === languageSelectRef.current.value);
        dispatch(actions.createDictionary(name, language))
    };

    const errors = Object.keys(errorMessages)
        .map(key => <Error key={`err-${key}`} message={errorMessages[key]} />)

    return (
        <Dialog close={close} confirmed={confirmed}>
            <fieldset className={classes.DictionaryForm}>
                <legend>{title}</legend>
                <div className={classes.FieldsWrapper}>
                    <label>Dictionary name:</label>
                    <input type="text" ref={nameInputRef} onKeyUp={verify} />
                    <label>Language:</label>
                    <select ref={languageSelectRef}>
                        {languages.map(lang => <option key={lang.code}>{lang.name}</option>)}
                    </select>
                </div>
            </fieldset>
            {errors}
        </Dialog>
    );
};

DictionaryForm.propTypes = {
    title: PropTypes.string,
    close: PropTypes.func.isRequired,
};

DictionaryForm.defaultProps = {
    title: '',
};

export default DictionaryForm;
