import { createSelector } from 'reselect';
import _ from 'lodash';

const simpleSelectorFactory = field => state => state.quizRecords[field];

export const quizRecordsOverviewsSelector = simpleSelectorFactory('quizRecordsOverviews');
export const selectedQuizRecordIndexSelector = simpleSelectorFactory('selectedQuizRecordIndex');
export const quizIsLoadingSelector = simpleSelectorFactory('loading');
export const loadedQuizRecordSelector = simpleSelectorFactory('loadedQuizRecord');
export const updatedQuizRecordSelector = simpleSelectorFactory('updatedQuizRecord');
export const quizRecordIsEditingSelector = simpleSelectorFactory('isEditing');
export const quizRecordEditModalTypeSelector = simpleSelectorFactory('quizRecordEditModalType');

export const showQuizRecordEditModalSelector = simpleSelectorFactory('showQuizRecordEditModal');

export const selectedQuizRecordSelector = createSelector(
    quizRecordsOverviewsSelector,
    selectedQuizRecordIndexSelector,
    (overviews, index) => index >= 0 ? overviews[index] : {},
);

export const quizRecordMainFormToUpdateSelector =
    state => _.get(state, 'quizRecords.updatedQuizRecord.wordMainForm', '');
