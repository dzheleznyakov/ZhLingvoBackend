import { createSelector } from 'reselect';

export const loadingQuizzesSelector = state => state.quizzes.loading;
export const quizzesSelector = state => state.quizzes.quizzes;
export const selectedQuizIndexSelector = state => state.quizzes.selectedQuizIndex;

export const quizzesTableDataSelector = createSelector(
    quizzesSelector,
    quizzes => quizzes.map(qz => ({
        name: {
            value: qz.name,
            id: qz.id,
        },
        language: { value: qz.targetLanguage.name },
    }))
);
