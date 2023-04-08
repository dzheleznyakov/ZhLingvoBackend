import React, { useState } from 'react';
import { useParams } from 'react-router-dom';
import { useSelector } from 'react-redux';


import * as actions from '../../../store/actions';
import * as selectors from '../../../store/selectors';
import { useConditionalActionOnMount, useQuizRunBreadcrumbs } from '../../../hooks';
import QuizForm from './QuizForm/QuizForm';
import { useQuizRunner } from './quizRunHooks';

const NULL_QUIZ = { id: -1, name: '', targetLanguage: {}};

const getNextQuestion = (quizRunner, records) => {
    if (quizRunner == null)
        return {
            quizRegime: null,
            record: null,
        };
    
    const { quizRegime, recordId } = quizRunner.next();
    const record = records.find(record => record.id === recordId);
    return { quizRegime, record };
};

const QuizRun = () => {
    const { qid } = useParams();
    const records = useSelector(selectors.allQuizRecordsSelector);
    const quiz = useSelector(selectors.loadedQuizSelector) || NULL_QUIZ;
    const quizSettings = useSelector(selectors.quizSettingsSelector)[quiz.id];
    const quizRunner = useQuizRunner(quizSettings, records);
    const [shouldRevealAnswer, setShouldRevealAnswer] = useState(false);

    useConditionalActionOnMount(actions.fetchQuiz(qid), qid != null, qid);
    useConditionalActionOnMount(actions.fetchQuizRecords(qid), qid != null, qid);
    useConditionalActionOnMount(actions.fetchQuizSettings(qid), qid != null, qid);

    useQuizRunBreadcrumbs(qid, quiz);

    const { quizRegime, record } = getNextQuestion(quizRunner, records);

    return (
        <div>
            {record && quizRegime && 
                <QuizForm record={record} quizRegime={quizRegime} />}
        </div>);
};

export default QuizRun;
