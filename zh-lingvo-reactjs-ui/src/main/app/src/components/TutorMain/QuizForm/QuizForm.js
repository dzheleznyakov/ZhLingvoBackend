import React, { useRef } from 'react';
import PropTypes from 'prop-types';
import { useSelector } from 'react-redux';

import { languagesSelector } from '../../../store/selectors';
import { useActionOnMount, useAutofocus } from '../../../hooks';
import * as actions from '../../../store/actions';
import { formInputTypes, validators, Form } from '../../UI';
import { quizType } from '../propTypes';

const QuizForm = props => {
    const { close, confirmed, title, quiz, disabledInputs } = props;
    const languages = useSelector(languagesSelector);

    useActionOnMount(actions.fetchAllLanguages());

    const defaultName = quiz.name || '';
    const defaultLanguage = quiz.targetLanguage.name || '';

    const quizGroup = {
        key: 'quiz',
        label: title,
    };

    const nameRef = useRef();
    const quizNameField = {
        key: 'name',
        label: 'Quiz name',
        type: formInputTypes.TEXT,
        defaultValue: defaultName,
        groupKey: quizGroup.key,
        forwardRef: nameRef,
        disabled: disabledInputs.name,
        validation: [{
            validate: validators.minLength(3),
            failureMessage: 'Quiz name should contain at least 3 characters',
        }],
    };

    const langRef = useRef();
    const langField = {
        key: 'lang',
        label: 'Target language',
        type: formInputTypes.SELECT,
        defaultValue: defaultLanguage,
        values: languages.map(({ name }) => name),
        groupKey: quizGroup.key,
        forwardRef: langRef,
        disabledInputs: disabledInputs.language,
    };

    useAutofocus(nameRef);

    const onConfirm = () => {
        const name = nameRef.current.value;
        const language = languages.find(lang => lang.name === langRef.current.value);
        confirmed(name, language);
    };

    return (
        <Form 
            fields={[quizNameField, langField]}
            groups={[quizGroup]}
            canceled={close}
            confirmed={onConfirm}
        />
    );
};

QuizForm.propTypes = {
    title: PropTypes.string,
    close: PropTypes.func.isRequired,
    confirmed: PropTypes.func.isRequired,
    quiz: quizType,
    disabledInputs: PropTypes.objectOf(PropTypes.bool),
};

QuizForm.defaultProps = {
    title: '',
    quiz: { targetLanguage: {} },
    disabledInputs: {},
};

export default QuizForm;
