import React, { useEffect, useRef, useState } from 'react';
import PropTypes from 'prop-types';
import { useSelector } from 'react-redux';

import classes from './DictionaryForm.module.scss';

import { Dialog, Error } from '../../UI';
import * as actions from '../../../store/actions';
import { dictionaryType } from '../propTypes';
import { languagesSelector } from '../../../store/selectors';
import { useActionOnMount, useAutofocus } from '../../../hooks';

const DictionaryForm = props => {
    const { close, confirmed, title, dictionary, disabledInputs } = props;
    const [errorMessages, setErrorMessages] = useState({});
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

    const onConfirm = () => {
        const name = nameInputRef.current.value;
        const language = languages.find(lang => lang.name === languageSelectRef.current.value);
        confirmed(name, language)
    };

    const defaultName = dictionary.name;
    const defaultLanguage = dictionary.language.name;

    const errors = Object.keys(errorMessages)
        .map(key => <Error key={`err-${key}`} message={errorMessages[key]} />)

    return (
        <Dialog close={close} confirmed={onConfirm}>
            <fieldset className={classes.DictionaryForm}>
                <legend>{title}</legend>
                <div className={classes.FieldsWrapper}>
                    <label>Dictionary name:</label>
                    <input 
                        type="text" 
                        ref={nameInputRef} 
                        onKeyUp={verify} 
                        defaultValue={defaultName}
                        disabled={disabledInputs.name}
                    />
                    <label>Language:</label>
                    <select 
                        ref={languageSelectRef} 
                        defaultValue={defaultLanguage}
                        disabled={disabledInputs.language}
                    >
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
    confirmed: PropTypes.func.isRequired,
    dictionary: dictionaryType,
    disabledInputs: PropTypes.objectOf(PropTypes.bool),
};

DictionaryForm.defaultProps = {
    title: '',
    dictionary: { language: {} },
    disabledInputs: {},
};

export default DictionaryForm;
