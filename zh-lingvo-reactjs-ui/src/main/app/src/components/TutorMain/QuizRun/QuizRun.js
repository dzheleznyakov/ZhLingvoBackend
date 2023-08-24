import React, { useEffect, useRef, useState } from 'react';
import { useParams } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';

import classes from './QuizRun.module.scss';

import * as actions from '../../../store/actions';
import * as selectors from '../../../store/selectors';
import { useConditionalActionOnMount, useQuizRunBreadcrumbs } from '../../../hooks';
import QuizForm from './QuizForm/QuizForm';
import { useQuizRunner } from './quizRunHooks';
import quizRegimes from '../../../static/constants/quizRegimes';
import { TUTOR_QUIZ_RUN_RESULT } from '../../../static/constants/paths';
import { Spinner } from '../../UI';

const NULL_QUIZ = { id: -1, name: '', targetLanguage: {}};

function getNextQuestion(quizRunner, records) {
    if (quizRunner == null)
        return {
            quizRegime: null,
            record: null,
        };
    
    const { quizRegime, recordId, done } = quizRunner.next();
    const record = records.find(record => record.id === recordId);
    return { quizRegime, record, done };
};

const QUIZ_PHASE = {
    QUESTION: 'QUESTION',
    ANSWER: 'ANSWER',
    COMPLETED: 'COMPLETED',
};

const QuizRun = () => {
    const { runid: runId, qid } = useParams();
    const dispatch = useDispatch();
    const records = useSelector(selectors.allQuizRecordsSelector);
    const quiz = useSelector(selectors.loadedQuizSelector) || NULL_QUIZ;
    const quizSettings = useSelector(selectors.quizSettingsSelector)[quiz.id];
    const quizRunner = useQuizRunner(quizSettings, records);
    const [quizRegime, setQuizRegime] = useState();
    const [record, setRecord] = useState();
    const [done, setDone] = useState(false);
    const [init, setInit] = useState(false);
    const [ready, setReady] = useState(false);
    const [phase, setPhase] = useState(QUIZ_PHASE.QUESTION);
    const [lastAnswerIsCorrect, setLastAnswerIsCorrect] = useState(false);
    const shouldRevealAnswer = phase === QUIZ_PHASE.ANSWER || phase === QUIZ_PHASE.COMPLETED;

    useConditionalActionOnMount(actions.fetchQuiz(qid), qid != null, qid);
    useConditionalActionOnMount(actions.fetchQuizRecords(qid), qid != null, qid);
    useConditionalActionOnMount(actions.fetchQuizSettings(qid), qid != null, qid);

    useQuizRunBreadcrumbs(qid, quiz);

    useEffect(() => {
        if (!init && quizRunner && records) {
            const nextQuestion = getNextQuestion(quizRunner, records);
            setQuizRegime(nextQuestion.quizRegime);
            setRecord(nextQuestion.record);
            setDone(nextQuestion.done);
            setInit(true);
        }
    }, [quizRunner, records, init]);

    useEffect(() => {
        if (!ready) 
            setReady(true);
    }, [ready]);

    const mainFormRef = useRef();
    const translationsRef = useRef();

    let buttonLabel;
    if (phase === QUIZ_PHASE.QUESTION)
        buttonLabel = 'Submit';
    else if (phase === QUIZ_PHASE.ANSWER)
        buttonLabel = 'Next';
    else
        buttonLabel = 'Finish';

    const answerRef = quizRegime === quizRegimes.BACKWARD ? mainFormRef : translationsRef;
    const onSubmitAnswer = () => {
        const answer = answerRef.current.value.trim().replaceAll(/\s+/g, ' ');
        const isCorrect = quizRunner.evaluate(answer);
        setLastAnswerIsCorrect(isCorrect);
        const nextPhase = done ? QUIZ_PHASE.COMPLETED : QUIZ_PHASE.ANSWER;
        setPhase(nextPhase);
        setReady(false);
        dispatch(actions.updateQuizRun(quizRunner.toQuizRun(), qid));
    };
    const onNextQuestion = () => {
        const nextQuestion = getNextQuestion(quizRunner, records);
        setQuizRegime(nextQuestion.quizRegime);
        setRecord(nextQuestion.record);
        setDone(nextQuestion.done);
        setPhase(QUIZ_PHASE.QUESTION);
        setReady(false);
    };
    const onCompleted = () => {
        dispatch(actions.navigateTo(
            TUTOR_QUIZ_RUN_RESULT
                .replace(/:qid/, qid)
                .replace(/:runid\?/, runId)));
        dispatch(actions.setQuizRun(quizRunner.toQuizRun()));
    };

    const onButtonClicked = () => {
        if (phase === QUIZ_PHASE.QUESTION)
            onSubmitAnswer();
        else if (phase === QUIZ_PHASE.ANSWER)
            onNextQuestion();
        else
            onCompleted();
    };

    const quizButtonRef = useRef();
    useEffect(() => {
        if (phase !== QUIZ_PHASE.QUESTION && ready) {
            quizButtonRef.current.focus();
        }
    }, [phase, ready]);
    const quizButton = (
        <button 
            ref={quizButtonRef}
            className={classes.QuizRunButton}
            onClick={onButtonClicked}
        >
            {buttonLabel}
        </button>
    );

    let indicatorLabel;
    const indicatorClasses = [classes.QuizRunIndicator];
    if (phase === QUIZ_PHASE.QUESTION) {
        indicatorLabel = '?';
        indicatorClasses.push(classes.IndicatorQuestion)
    }
    else if (lastAnswerIsCorrect) {
        indicatorLabel = '✓';
        indicatorClasses.push(classes.IndicatorCorrect);
    }
    else {
        indicatorLabel = '✗';
        indicatorClasses.push(classes.IndicatorIncorrect);
    }
    const indicator = <div className={indicatorClasses.join(' ')}>{indicatorLabel}</div>;
    
    return runId == null ? <Spinner /> : (
        <div className={classes.QuizRunWrapper}>
            <div className={classes.QuizRunContent}>
                {ready && record && quizRegime && 
                    <QuizForm 
                        record={record} 
                        quizRegime={quizRegime} 
                        shouldRevealAnswer={shouldRevealAnswer}
                        mainFormRef={mainFormRef}
                        translationsRef={translationsRef}
                        onSubmit={onSubmitAnswer}
                    />
                }
                <div className={classes.QuizRunControl}>
                    {indicator}
                    {quizButton}
                </div>
            </div>
        </div>);
};

export default QuizRun;
