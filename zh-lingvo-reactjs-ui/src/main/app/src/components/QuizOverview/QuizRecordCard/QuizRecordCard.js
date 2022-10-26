import React from 'react';
import PropTypes from 'prop-types';
import { useParams } from 'react-router';
import { useSelector } from 'react-redux';

import classes from './QuizRecordCard.module.scss';

import * as selectors from '../../../store/selectors';
import * as actions from '../../../store/actions';
import { useConditionalActionOnMount } from '../../../hooks';
import { Spinner } from '../../UI';
import { WordMainForm } from './parts';

const QuizRecordCard = props => {
    const { qid, rid } = useParams();
    const quizId = +qid;
    const recordId = +rid;
    const quizIsLoading = useSelector(selectors.quizIsLoadingSelector);
    const loadedQuizRecord = useSelector(selectors.loadedQuizRecordSelector) || {};
    const { wordMainForm } = loadedQuizRecord;
    

    useConditionalActionOnMount(
        actions.fetchQuizRecord(quizId, recordId),
        !Number.isNaN(quizId) && !Number.isNaN(recordId),
        quizId, recordId);

    switch (true) {
        case quizIsLoading: return <Spinner />;
        case !!wordMainForm: return (
            <div className={classes.QuizRecordCardWrapper}>
                <div className={classes.QuizRecordViewWrapper}>
                    <WordMainForm>{wordMainForm}</WordMainForm>
                </div>
            </div>
        );
        default: return null;
    }
};

QuizRecordCard.propTypes = {};

QuizRecordCard.defaultProps = {};

export default QuizRecordCard;
