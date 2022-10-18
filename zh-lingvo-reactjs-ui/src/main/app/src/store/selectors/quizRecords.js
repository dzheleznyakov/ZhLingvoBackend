import { createSelector } from 'reselect';

const simpleSelectorFactory = field => state => state.quizRecords[field];

export const quizRecordsOverviewsSelector = simpleSelectorFactory('quizRecordsOverviews');
export const selectedQuizRecordIndexSelector = simpleSelectorFactory('selectedQuizRecordIndex');
export const quizIsLoadingSelector = simpleSelectorFactory('loading');
export const loadedQuizRecordSelector = simpleSelectorFactory('loadedQuizRecord');

export const selectedQuizRecordSelector = createSelector(
    quizRecordsOverviewsSelector,
    selectedQuizRecordIndexSelector,
    (overviews, index) => index >= 0 ? overviews[index] : {},
);
