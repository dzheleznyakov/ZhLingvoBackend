import * as actionTypes from '../actionTypes/quizRecords';

export const fetchQuizRecords = quizId => ({
    type: actionTypes.FETCH_QUIZ_RECORDS,
    quizId,
})

export const fetchQuizRecordsSuccess = overviews => ({
    type: actionTypes.FETCH_QUIZ_RECORDS_SUCCESS,
    overviews,
});

export const selectQuizRecord = index => ({
    type: actionTypes.SELECT_QUIZ_RECORD,
    index,
});
