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

export const createQuizRecord = (quizId, wordMainForm, pos) => ({
    type: actionTypes.CREATE_QUIZ_RECORD,
    quizId,
    wordMainForm,
    pos,
});

export const deleteQuizRecord = (quizId, recordId) => ({
    type: actionTypes.DELETE_QUIZ_RECORD,
    quizId,
    recordId,
});

export const fetchQuizRecord = (quizId, recordId) => ({
    type: actionTypes.FETCH_QUIZ_RECORD,
    quizId,
    recordId,
});

export const fetchQuizRecordStart = () => ({
    type: actionTypes.FETCH_QUIZ_RECORD_START,
});

export const fetchQuizRecordSuccess = quizRecord => ({
    type: actionTypes.FETCH_QUIZ_RECORD_SUCCESS,
    quizRecord,
});

export const fetchQuizRecordFailure = () => ({
    type: actionTypes.FETCH_QUIZ_RECORD_FAILURE,
});
