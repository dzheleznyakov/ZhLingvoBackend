import React from 'react';
import { useParams } from 'react-router-dom';
import { useSelector } from 'react-redux';

import classes from './QuizRunResult.module.scss';

import { allQuizRecordsSelector, quizRunSelector } from '../../../../store/selectors';
import { CollapsableListView } from '../../../UI';
import { useActionOnMount } from '../../../../hooks';
import * as actions from '../../../../store/actions';

const QuizRunResult = () => {
    const { qid } = useParams();
    const quizRun = useSelector(quizRunSelector);
    const records = useSelector(allQuizRecordsSelector) || [];

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

    const summary = qid && quizRun && records.length && (
        <div className={classes.QuizRunResultContent}>
            Number of questions in the run:{' '}
            {quizRun.doneRecords.length}
            <CollapsableListView heading='Correct answers:' items={correctAswers} />
            <CollapsableListView heading='Incorrect answers:' items={incorrectAnswers} />
        </div>
    );

    return (
        <div className={classes.QuizRunResultWrapper}>
            {summary}
        </div>
    );
};

export default QuizRunResult;
