import { createSelector } from 'reselect';
import _ from 'lodash';

export const loadingQuizzesSelector = state => state.quizzes.loading;
export const quizzesSelector = state => state.quizzes.quizzes;
export const selectedQuizIndexSelector = state => state.quizzes.selectedQuizIndex;
export const loadedQuizSelector = state => state.quizzes.loadedQuiz;
export const matchingRegimesSelector = state => state.quizzes.matchingRegimes;
export const quizRegimeSelector = state => state.quizzes.quizRegimes;
export const quizSettingsSelector = state => state.quizzes.settings;

export const selectedQuizSelector = state => {
    const { selectedQuizIndex, quizzes} = state.quizzes;
    return selectedQuizIndex < 0
        ? { targetLanguage: {} }
        : quizzes[selectedQuizIndex];
};

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

export const meaningToQuizRecordSelector = state => state.quizzes.meaningToQuizRecord;

export const quizRunSelector = state => state.quizzes.quizRun;
export const quizRunTargetLanguageSelector = state => 
    _.get(state, 'quizzes.quizRun.targetLanguage.code')
    || _.get(state, 'quizzes.loadedQuiz.targetLanguage.code');
export const allQuizRunsSelector = state => state.quizzes.quizRuns;
