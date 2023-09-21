import React, { useState } from 'react';
import PropTypes from 'prop-types';
import { useSelector } from 'react-redux';
import { useParams } from 'react-router-dom';

import QUIZ_REGIMES from '../../../../static/constants/quizRegimes';
import { useActionOnMount, useAutofocus } from '../../../../hooks';
import { FormBase, formInputTypes } from '../../../UI';
import { refType } from '../../../../static/types/generalTypes';
import { quizRunSelector, selectedQuizSelector } from '../../../../store/selectors';
import * as actions from '../../../../store/actions';

const quizFormTranslationType = PropTypes.shape({
    value: PropTypes.string.isRequired,
    elaboration: PropTypes.string,
});

const quizFormExampleType = PropTypes.shape({
    expression: PropTypes.string.isRequired,
    explanation: PropTypes.string.isRequired,
});

const quizFormRecordType = PropTypes.shape({
    wordMainForm: PropTypes.string.isRequired,
    transcription: PropTypes.string,
    translations: PropTypes.arrayOf(quizFormTranslationType).isRequired,
    examples: PropTypes.arrayOf(quizFormExampleType).isRequired,
});

const quizRegimesArray = Object.keys(QUIZ_REGIMES).map(key => QUIZ_REGIMES[key]);

function getMainFormValue(quizRegime, shouldRevealAnswer, record) {
    return shouldRevealAnswer || quizRegime === QUIZ_REGIMES.FORWARD 
        ? record.wordMainForm 
        : '';
}

function getTranscriptionValue(quizRegime, shouldRevealAnswer, record) {
    return shouldRevealAnswer || quizRegime === QUIZ_REGIMES.FORWARD 
        ? `[${record.transcription}]` 
        : '';
}

function getTranslationsValue(quizRegime, shouldRevealAnswer, record) {
    return shouldRevealAnswer || quizRegime === QUIZ_REGIMES.BACKWARD
        ? record.translations
            .map(({ value, elaboration }) => `${value}${elaboration ? ` (${elaboration})` : ''}`)
            .join('; ')
        : '';
}

function getExamplesValue(quizRegime, shouldRevealAnswer, record) {
    if (shouldRevealAnswer)
        return record.examples
            .map(({ expression, explanation}) => `${expression} — ${explanation}`)
            .join('\n\n');
    
    if (quizRegime === QUIZ_REGIMES.FORWARD)
        return record.examples
            .map(({ expression }) => expression)
            .join('\n\n');

    return record.examples
        .map(({ explanation }) => explanation)
        .join('\n\n');
}

const ENTER_KEY_CODE = 13;

const QuizForm = props => {
    const { 
        record, 
        quizRegime, 
        shouldRevealAnswer, 
        mainFormRef,
        translationsRef,
        onSubmit,
    } = props;

    const quizRun = useSelector(quizRunSelector);
    const langCode = quizRun && quizRun.targetLanguage ? quizRun.targetLanguage.code : null;

    const quizFormGroup = {
        key: 'quizFormGroup',
        label: 'Quiz',
    };

    const mainFormListeners = {};
    if (quizRegime === QUIZ_REGIMES.BACKWARD) {
        mainFormListeners.onKeyDown = event => {
            if (event.keyCode === ENTER_KEY_CODE) {
                event.preventDefault();
                onSubmit();
            }
        };
        // mainFormListeners.onKeyUp = event => {
        //     if (langCode == null)
        //         return;
        //     event.preventDefault();
        //     const { value } = mainFormLog;
        //     const updatedValue = value + 'x';
        //     event.target.value = updatedValue;
        //     setMainFormLog({ ...mainFormLog, value: updatedValue });
        // };
    }
    const mainFormField = {
        key: 'mainFormField',
        label: 'Word',
        type: quizRegime === QUIZ_REGIMES.FORWARD ? formInputTypes.TEXT : formInputTypes.LOCALE_TEXT,
        defaultValue: getMainFormValue(quizRegime, shouldRevealAnswer, record),
        groupKey: quizFormGroup.key,
        forwardRef: mainFormRef,
        disabled: shouldRevealAnswer || quizRegime !== QUIZ_REGIMES.BACKWARD,
        autocomplete: false,
        listeners: mainFormListeners,
    };

    const posField = {
        key: 'posField',
        label: 'Part of Speech',
        type: formInputTypes.TEXT,
        defaultValue: record && record.pos,
        groupKey: quizFormGroup.key,
        forwardRef: null,
        disabled: true,
    };
    
    const transcriptionField = {
        key: 'transcriptionField',
        label: 'Transcription',
        type: formInputTypes.TEXT,
        defaultValue: getTranscriptionValue(quizRegime, shouldRevealAnswer, record),
        groupKey: quizFormGroup.key,
        forwardRef: null,
        disabled: true,
    };

    const translationsListeners = {};
    if (quizRegime === QUIZ_REGIMES.FORWARD)
        translationsListeners.onKeyDown = event => {
            if (event.keyCode === ENTER_KEY_CODE) {
                event.preventDefault();
                onSubmit();
            }
        }
    const translationsField = {
        key: 'translationsField',
        label: 'Translation',
        type: formInputTypes.TEXT_AREA,
        defaultValue: getTranslationsValue(quizRegime, shouldRevealAnswer, record),
        groupKey: quizFormGroup.key,
        forwardRef: translationsRef,
        disabled: shouldRevealAnswer || quizRegime !== QUIZ_REGIMES.FORWARD,
        autocomplete: false,
        listeners: translationsListeners,
    };

    const examplesField = {
        key: 'examplesField',
        label: 'Examples',
        type: formInputTypes.TEXT_AREA,
        defaultValue: getExamplesValue(quizRegime, shouldRevealAnswer, record),
        groupKey: quizFormGroup.key,
        forwardRef: null,
        disabled: true,
    };

    const fields = [mainFormField, posField];
    if (record && record.transcription)
        fields.push(transcriptionField);
    fields.push(translationsField);
    if (record && record.examples.length)
        fields.push(examplesField);

    const autofocusRef = quizRegime === QUIZ_REGIMES.BACKWARD
        ? mainFormRef
        : translationsRef;
    useAutofocus(autofocusRef);

    return (
        <FormBase
            fields={fields}
            groups={[quizFormGroup]}
        />
    );
};

QuizForm.propTypes = {
    record: quizFormRecordType.isRequired,
    quizRegime: PropTypes.oneOf(quizRegimesArray).isRequired,
    shouldRevealAnswer: PropTypes.bool,
    mainFormRef: refType.isRequired,
    translationsRef: refType.isRequired,
    onSubmit: PropTypes.func.isRequired,
};

QuizForm.defaultProps = {
    shouldRevealAnswer: false,
};

export default QuizForm;
