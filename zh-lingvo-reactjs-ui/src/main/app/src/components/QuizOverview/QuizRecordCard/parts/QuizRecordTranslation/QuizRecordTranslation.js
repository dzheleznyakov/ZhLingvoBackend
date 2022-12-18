import React from 'react';
import PropTypes from 'prop-types';

import classes from './QuizRecordTranslation.module.scss';

import { quizRecordTranslationType } from '../../types';

const QuizRecordTranslation = props => {
    const { entry: translation, postfix } = props;
    const { value, elaboration } = translation;
    const elaborationComp = elaboration ? (
        <span className={classes.Elaboration}> ({elaboration})</span>
    ) : null;

    return <span>{value}{elaborationComp}{postfix}</span>;
};

QuizRecordTranslation.propTypes = {
    entry: quizRecordTranslationType.isRequired,
    postfix: PropTypes.string,
};

export default QuizRecordTranslation;
