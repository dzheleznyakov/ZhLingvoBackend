import React from 'react';
import PropTypes from 'prop-types';

import classes from './QuizRecordView.module.scss';

import { quizRecordExampleType, quizRecordTranslationType } from '../types';
import { QuizRecordExample, QuizRecordTranslation, Transcription } from '../parts';

const QuizRecordView = props => {
    const { quizRecord } = props;
    const { transcription, translations, examples } = quizRecord;

    const transcriptionElement = transcription 
        && <Transcription>{transcription}</Transcription>;

    const translationElements = translations
        .map((translation, index) => <QuizRecordTranslation 
            key={`qr_translation-${translation.id}`}
            translation={translation}
            postfix={index < translations.length - 1 ? '; ' : null}
        />);

    const exampleElements = examples
            .map(example => <QuizRecordExample 
                key={`qr_example-${example.id}`}
                example={example}
            />);

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
