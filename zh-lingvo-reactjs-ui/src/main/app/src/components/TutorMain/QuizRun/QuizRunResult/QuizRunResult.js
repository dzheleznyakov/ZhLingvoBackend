import React from 'react';
import { useParams } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';

import classes from './QuizRunResult.module.scss';

import { allQuizRecordsSelector, quizRunSelector } from '../../../../store/selectors';
import { CollapsableListView } from '../../../UI';
import { useActionOnMount } from '../../../../hooks';
import * as actions from '../../../../store/actions';
import { TUTOR_QUIZ } from '../../../../static/constants/paths';

const QuizRunResult = () => {
    const { qid } = useParams();
    const quizRun = useSelector(quizRunSelector);
    const records = useSelector(allQuizRecordsSelector) || [];
    const dispatch = useDispatch();

    useActionOnMount(actions.completeQuizRun(quizRun, qid));

    const correctAswers = quizRun && records.length 
        ? quizRun.doneRecords
            .filter(({ correct }) => correct)
            .map(({ recordId }) => {
                const record = records.find(r => r.id === recordId);
                return {
                    key: recordId,
                    node: record ? record.wordMainForm : '',
                };
            })
        : [];

    const incorrectAnswers = quizRun && records.length
            ? quizRun.doneRecords
                .filter(({ correct }) => !correct)
                .map(({ recordId }) => {
                    const record = records.find(r => r.id === recordId);
                    return {
                        key: recordId,
                        node: record ? record.wordMainForm : '',
                    };
                })
            : [];
    
    const onBackToQuizClicked = () => {
        const url = TUTOR_QUIZ.replace(/:(\w)+\\?/g, param => {
            switch (param) {
                case ':qid': return qid;
                case ':rid': return '';
            }
        })
        dispatch(actions.navigateTo(url));
    };

    const summary = qid && quizRun && records.length && (
        <div className={classes.QuizRunResultContent}>
            Number of questions in the run:{' '}
            {quizRun.doneRecords.length}
            <CollapsableListView heading='Correct answers:' items={correctAswers} />
            <CollapsableListView heading='Incorrect answers:' items={incorrectAnswers} />
            <button 
                className={classes.BackButton}
                onClick={onBackToQuizClicked}
            >Back to quiz</button>
        </div>
    );

    return (
        <div className={classes.QuizRunResultWrapper}>
            {summary}
        </div>
    );
};

export default QuizRunResult;
