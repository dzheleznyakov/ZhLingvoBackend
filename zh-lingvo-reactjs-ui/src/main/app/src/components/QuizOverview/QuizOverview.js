import React, { useEffect } from 'react';
import { useParams } from 'react-router';
import { useDispatch, useSelector } from 'react-redux';

import classes from './QuizOverview.module.scss';

import { Spinner } from '../UI';
import * as actions from '../../store/actions';
import * as selectors from '../../store/selectors';
import { useActionOnMount, useTutorQuizOverivewBreadcrumbs } from '../../hooks';
import RecordList from './RecordList/RecordList';
import QuizRecordCard from './QuizRecordCard/QuizRecordCard';

const QuizOverview = () => {
    const { qid: quizId } = useParams();
    const dispatch = useDispatch();
    const recordsCount = useSelector(selectors.quizRecordsOverviewsCountSelector);
    useActionOnMount(actions.fetchQuiz(+quizId));

    const quiz = useSelector(selectors.loadedQuizSelector);
    const { code: languageCode } = (quiz || {}).targetLanguage || {};
    const quizName = (quiz && quiz.name) || '';
    
    useTutorQuizOverivewBreadcrumbs();

    useEffect(() => {
        if (languageCode)
            dispatch(actions.fetchPos(languageCode));
    }, [languageCode, dispatch]);

    const quizIsLoading = useSelector(selectors.loadingQuizzesSelector);

    return (
        <div>
            <h1 className={classes.QuizName}>{quizName}</h1>
            <h2 className={classes.LanguageName}>{quiz && quiz.targetLanguage.name}</h2>
            <h2 className={classes.RecordsCount}>Number of records: {recordsCount}</h2>
            {quizIsLoading && <Spinner />}
            <div className={classes.ContentWrapper}>
                <RecordList />
                <QuizRecordCard />
            </div>
        </div>
    );
};

export default QuizOverview;

