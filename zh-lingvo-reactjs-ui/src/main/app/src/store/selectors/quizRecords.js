const simpleSelectorFactory = field => state => state.quizRecords[field];

export const quizRecordsOverviewsSelector = simpleSelectorFactory('quizRecordsOverviews');
export const quizIsLoadingSelector = simpleSelectorFactory('loading');
