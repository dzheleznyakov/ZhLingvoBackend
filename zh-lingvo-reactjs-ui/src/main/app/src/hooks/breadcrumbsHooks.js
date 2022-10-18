import { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useParams } from 'react-router';

import { setBreadcrumbs, navigateTo } from '../store/actions';
import { loadedQuizSelector, selectedQuizRecordSelector } from '../store/selectors';
import { BREADCRUMBS_TYPES } from '../utils/breadcrumbs';

export const useBreadcrumbs = (...breadcrumbs) => {
    const dispatch = useDispatch();
    useEffect(() => {
        dispatch(setBreadcrumbs(breadcrumbs));
    }, [dispatch]);
};

export const useDynamicBreadcrumbs = (deps, ...breadcrumbs) => {
    const dispatch = useDispatch();
    const dependencies = [dispatch].concat(deps);
    useEffect(() => {
        dispatch(setBreadcrumbs(breadcrumbs));
    }, dependencies);
}

export const useTutorQuizOverivewBreadcrumbs = () => {
    const dispatch = useDispatch();
    const { qid: quizId, rid: recordId } = useParams();
    
    const quiz = useSelector(loadedQuizSelector);
    const quizName = (quiz && quiz.name) || '';

    const selectedQuizRecord = useSelector(selectedQuizRecordSelector) || {};
    const { wordMainForm = '' } = selectedQuizRecord;

    const breadcrumbs = [{
        type: BREADCRUMBS_TYPES.URL,
        text: 'Tutor',
        href: '/tutor',
        onClick: () => dispatch(navigateTo('/tutor')),
    }];

    if (recordId !== null && recordId !== undefined) {
        if (quizName) {
            const quizPath = `/tutor/quiz/${quizId}`;
            breadcrumbs.push({
                type: BREADCRUMBS_TYPES.URL,
                text: quizName,
                href: quizPath,
                onClick: () => dispatch(navigateTo(quizPath))
            });
        }
        if (wordMainForm)
            breadcrumbs.push({
                type: BREADCRUMBS_TYPES.TEXT,
                text: wordMainForm,
            });
    } else {
        breadcrumbs.push({
            type: BREADCRUMBS_TYPES.TEXT,
            text: quizName,
        });
    }

    useDynamicBreadcrumbs([quizName, recordId], ...breadcrumbs);
}
