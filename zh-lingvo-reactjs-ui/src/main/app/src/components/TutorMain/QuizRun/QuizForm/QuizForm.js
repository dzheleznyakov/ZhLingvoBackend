import React, { useRef } from 'react';
import PropTypes from 'prop-types';

import QUIZ_REGIMES from '../../../../static/constants/quizRegimes';
import { useAutofocus } from '../../../../hooks';
import { FormBase, formInputTypes } from '../../../UI';

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

const QuizForm = props => {
    const { record, quizRegime, shouldRevealAnswer } = props;
    const quizFormGroup = {
        key: 'quizFormGroup',
        label: 'Quiz',
    };

    const mainFormRef = useRef();
    const mainFormField = {
        key: 'mainFormField',
        label: 'Word',
        type: formInputTypes.TEXT,
        defaultValue: getMainFormValue(quizRegime, shouldRevealAnswer, record),
        groupKey: quizFormGroup.key,
        forwardRef: mainFormRef,
        disabled: true,
    };
    
    const transcriptionRef = useRef();
    const transcriptionField = {
        key: 'transcriptionField',
        label: 'Transcription',
        type: formInputTypes.TEXT,
        defaultValue: getTranscriptionValue(quizRegime, shouldRevealAnswer, record),
        groupKey: quizFormGroup.key,
        forwardRef: transcriptionRef,
        disabled: true,
    };

    const translationsRef = useRef();
    const translationsField = {
        key: 'translationsField',
        label: 'Translation',
        type: formInputTypes.TEXT_AREA,
        defaultValue: getTranslationsValue(quizRegime, shouldRevealAnswer, record),
        groupKey: quizFormGroup.key,
        forwardRef: translationsRef,
        disabled: true,
    };

    const examplesRef = useRef();
    const examplesField = {
        key: 'examplesField',
        label: 'Examples',
        type: formInputTypes.TEXT_AREA,
        defaultValue: getExamplesValue(quizRegime, shouldRevealAnswer, record),
        groupKey: quizFormGroup.key,
        forwardRef: examplesRef,
        disabled: true,
    };

    const fields = [mainFormField];
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
};

QuizForm.defaultProps = {
    shouldRevealAnswer: false,
};

export default QuizForm;
