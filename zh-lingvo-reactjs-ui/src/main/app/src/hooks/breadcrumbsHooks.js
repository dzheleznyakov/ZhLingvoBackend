import { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useParams } from 'react-router';

import { setBreadcrumbs, navigateTo } from '../store/actions';
import { loadedQuizSelector } from '../store/selectors';
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
    const { qid: quizId, rid: recordId } = useParams();
    const quiz = useSelector(loadedQuizSelector);
    const quizName = (quiz && quiz.name) || '';
    const dispatch = useDispatch();

    const breadcrumbs = [{
        type: BREADCRUMBS_TYPES.URL,
        text: 'Tutor',
        href: '/tutor',
        onClick: () => dispatch(navigateTo('/tutor')),
    }];

    if (recordId !== null && recordId !== undefined) {
        const quizPath = `/tutor/quiz/${quizId}`;
        breadcrumbs.push({
            type: BREADCRUMBS_TYPES.URL,
            text: quizName,
            href: quizPath,
            onClick: () => dispatch(navigateTo(quizPath))
        });
        breadcrumbs.push({
            type: BREADCRUMBS_TYPES.TEXT,
            text: 'word',
        });
    } else {
        breadcrumbs.push({
            type: BREADCRUMBS_TYPES.TEXT,
            text: quizName,
        });
    }

    useDynamicBreadcrumbs([quizName, recordId], ...breadcrumbs);
}
