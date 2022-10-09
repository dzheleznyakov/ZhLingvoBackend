import React, { useEffect } from 'react';
import { useParams } from 'react-router';
import { useDispatch, useSelector } from 'react-redux';

import classes from './QuizOverview.module.scss';

import { Spinner } from '../UI';
import { BREADCRUMBS_TYPES } from '../../utils/breadcrumbs';
import * as actions from '../../store/actions';
import * as selectors from '../../store/selectors';
import { useActionOnMount, useDynamicBreadcrumbs, useTutorQuizOverivewBreadcrumbs } from '../../hooks';
import RecordList from './RecordList/RecordList';
import QuizRecordCard from './QuizRecordCard/QuizRecordCard';

// const BREADCRUMBS_GETTER = (quizName, toHome) => [{
    // type: BREADCRUMBS_TYPES.URL,
    // text: 'Tutor',
    // href: '/tutor',
    // onClick: toHome,
// }, {
    // type: BREADCRUMBS_TYPES.TEXT,
    // text: quizName,
// }];

const QuizOverview = () => {
    const { qid: quizId } = useParams();
    const dispatch = useDispatch();
    useActionOnMount(actions.fetchQuiz(+quizId));

    const quiz = useSelector(selectors.loadedQuizSelector);
    const { code: languageCode } = (quiz || {}).targetLanguage || {};
    const quizName = (quiz && quiz.name) || '';
    // const breadcrumbs = BREADCRUMBS_GETTER(
        // quizName,
        // () => dispatch(actions.navigateTo('/tutor')),
    // );
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
            {quizIsLoading && <Spinner />}
            <div className={classes.ContentWrapper}>
                <RecordList />
                <QuizRecordCard />
            </div>
        </div>
    );
};

export default QuizOverview;

