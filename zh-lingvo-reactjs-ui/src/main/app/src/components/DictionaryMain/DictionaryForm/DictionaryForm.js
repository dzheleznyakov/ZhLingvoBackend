import React, { useRef } from 'react';
import PropTypes from 'prop-types';
import { useSelector } from 'react-redux';

import { Form, formInputTypes, validators } from '../../UI';
import * as actions from '../../../store/actions';
import { dictionaryType } from '../propTypes';
import { languagesSelector } from '../../../store/selectors';
import { useActionOnMount, useAutofocus } from '../../../hooks';

const DictionaryForm = props => {
    const { close, confirmed, title, dictionary, disabledInputs } = props;
    const languages = useSelector(languagesSelector);

    useActionOnMount(actions.fetchAllLanguages());

    const defaultName = dictionary.name;
    const defaultLanguage = dictionary.language.name;

    const dictionaryGroup = {
        key: 'dictionary',
        label: title,
    };

    const nameRef = useRef();
    const dicNameField = {
        key: 'name',
        label: 'Dictionary name',
        type: formInputTypes.TEXT,
        defaultValue: defaultName || '',
        groupKey: dictionaryGroup.key,
        forwardRef: nameRef,
        disabled: disabledInputs.name,
        validation: [{
            validate: validators.minLength(3),
            failureMessage: 'Dicitonary name should contain at least 3 characters',
        }],
    };

    const langRef = useRef();
    const langField = {
        key: 'lang',
        label: 'Language',
        type: formInputTypes.SELECT,
        defaultValue: defaultLanguage || '',
        values: languages.map(({ name }) => name),
        groupKey: dictionaryGroup.key,
        forwardRef: langRef,
        disabled: disabledInputs.language,
    };

    useAutofocus(nameRef);

    const onConfirm = () => {
        const name = nameRef.current.value;
        const language = languages.find(lang => lang.name === langRef.current.value);
        confirmed(name, language)
    };

    return (
        <Form 
            fields={[dicNameField, langField]}
            groups={[dictionaryGroup]}
            canceled={close}
            confirmed={onConfirm}
        />
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
