import React from 'react';
import { useParams } from 'react-router';
import { useSelector } from 'react-redux';

import classes from './QuizRecordCard.module.scss';

import * as selectors from '../../../store/selectors';
import * as actions from '../../../store/actions';
import { useConditionalActionOnMount } from '../../../hooks';
import { Spinner } from '../../UI';
import { WordMainForm } from './parts';
import QuizRecordView from './QuizRecordView/QuizRecordView';
import QuizRecordControl from './QuizRecordControl/QuizRecordControl';

const NULL_QUIZ_RECORD = {};

const QuizRecordCard = () => {
    const { qid, rid } = useParams();
    const quizId = +qid;
    const recordId = +rid;
    const quizIsLoading = useSelector(selectors.quizIsLoadingSelector);
    const loadedQuizRecord = useSelector(selectors.loadedQuizRecordSelector) || NULL_QUIZ_RECORD;
    const { wordMainForm } = loadedQuizRecord;
    

    useConditionalActionOnMount(
        actions.fetchQuizRecord(quizId, recordId),
        !Number.isNaN(quizId) && !Number.isNaN(recordId),
        quizId, recordId);

    switch (true) {
        case quizIsLoading: return <Spinner />;
        case !!rid && !!wordMainForm: return (
            <div className={classes.QuizRecordCardWrapper}>
                <div className={classes.QuizRecordViewWrapper}>
                    <WordMainForm>{wordMainForm}</WordMainForm>
                {loadedQuizRecord !== NULL_QUIZ_RECORD && <QuizRecordView quizRecord={loadedQuizRecord} />}
                </div>
            {loadedQuizRecord !== NULL_QUIZ_RECORD && <QuizRecordControl />}
            </div>
        );
        default: return null;
    }
};

export default QuizRecordCard;
