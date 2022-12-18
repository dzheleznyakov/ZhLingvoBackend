import React from 'react';
import PropTypes from 'prop-types';
import { useSelector } from 'react-redux';

import classes from './QuizRecordView.module.scss';

import { quizRecordExampleType, quizRecordTranslationType } from '../types';
import { QuizRecordExample, QuizRecordTranslation } from '../parts';
import EditableTranscription from '../parts/Transcription/EditableTranscription';
import EditableQuizRecordTranslation, { NULL_QUIZ_RECORD_TRANSLATION } from '../parts/QuizRecordTranslation/EditableQuizRecordTranslation';
import { quizRecordIsEditingSelector } from '../../../../store/selectors';
import EditableQuizRecordExample, { NULL_EXAMPLE } from '../parts/QuizRecordExample/EditableQuizRecordExample';

const TRANSCRIPTION_PATH_SEGMENT = 'transcription';
const TRANSLATIONS_PATH_SEGMENT = 'translations';
const EXAMPLES_PATH_SEGMENT = 'examples';

const getTranslations = (translations, path, isEditing, editable) => {
    const TranslationComp = editable 
        ? EditableQuizRecordTranslation 
        : QuizRecordTranslation;
    const translationComponents = (translations || [])
        .map((tr, i) => (
            <TranslationComp
                key={`qr_translation-${tr.id}`}
                path={[...path, TRANSLATIONS_PATH_SEGMENT, `${i}`]}
                entry={tr}
                postfix={i < translations.length - 1 ? '; ' : ' '}
            />
        ));
    if (editable && isEditing)
        translationComponents.push(<EditableQuizRecordTranslation
            key="translation-new"
            path={[...path, TRANSLATIONS_PATH_SEGMENT, `${translations.length}`]}
            entry={NULL_QUIZ_RECORD_TRANSLATION}
        />)
    
    return translationComponents;
}

const getExamples = (examples, path, isEditing, editable) => {
    const ExampleComp = editable ? EditableQuizRecordExample : QuizRecordExample;
    const exampleComponents = (examples || [])
        .map((ex, i) => (
            <ExampleComp
                key={ex.id}
                path={[...path, EXAMPLES_PATH_SEGMENT, `${i}`]}
                entry={ex}
            />
        ));
    
    if (editable && isEditing)
        exampleComponents.push(<EditableQuizRecordExample 
            key="example-new"
            path={[...path, EXAMPLES_PATH_SEGMENT, `${examples.length}`]}
            entry={NULL_EXAMPLE}
        />);

    return exampleComponents;
};

const QuizRecordView = props => {
    const { quizRecord } = props;
    const { transcription, translations, examples } = quizRecord;
    const isEditing = useSelector(quizRecordIsEditingSelector);

    const transcriptionElement = (
        <EditableTranscription path={[TRANSCRIPTION_PATH_SEGMENT]}>
            {transcription}
        </EditableTranscription>
    );

    const translationElements = getTranslations(translations, [], isEditing, true);
    const exampleElements = getExamples(examples, [], isEditing, true);

    return (
        <div className={classes.QuizRecoedView}>
            {transcriptionElement}
            {translationElements}
            {exampleElements}
        </div>
    );
};

QuizRecordView.propTypes = {
    quizRecord: PropTypes.shape({
        transcription: PropTypes.string,
        pos: PropTypes.string.isRequired,
        translations: PropTypes.arrayOf(quizRecordTranslationType).isRequired,
        examples: PropTypes.arrayOf(quizRecordExampleType).isRequired
    }),
};

export default QuizRecordView;
