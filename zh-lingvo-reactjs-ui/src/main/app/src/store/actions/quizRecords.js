import * as actionTypes from '../actionTypes/quizRecords';

export const fetchQuizRecordsOverviews = quizId => ({
    type: actionTypes.FETCH_QUIZ_RECORDS_OVERVIEWS,
    quizId,
})

export const fetchQuizRecordsOverviewsSuccess = overviews => ({
    type: actionTypes.FETCH_QUIZ_RECORDS_OVERVIEWS_SUCCESS,
    overviews,
});

export const fetchQuizRecords = quizId => ({
    type: actionTypes.FETCH_QUIZ_RECORDS,
    quizId,
});

export const fetchQuizRecordsSuccess = records => ({
    type: actionTypes.FETCH_QUIZ_RECORDS_SUCCESS,
    records,
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

export const setQuizRecordEditing = isEditing => ({
    type: actionTypes.SET_QUIZ_RECORD_EDITING,
    isEditing,
});

export const shouldShowQuizRecordEditModal = show => ({
    type: actionTypes.SHOULD_SHOW_QUIZ_RECORD_EDIT_MODAL,
    show,
});

export const updateQuizRecordMainForm = updatedMainForm => ({
    type: actionTypes.UPDATE_QUIZ_RECORD_MAIN_FORM,
    updatedMainForm,
});

export const updateQuizRecordElement = (path, value) => ({
    type: actionTypes.UPDATE_QUIZ_RECORD_ELEMENT,
    path,
    value,
})

export const setQuizRecordEditModalType = (modalType, path) => ({
    type: actionTypes.SET_QUIZ_RECORD_EDIT_MODAL_TYPE,
    modalType,
    path,
});

export const updateQuizRecord = quizId => ({
    type: actionTypes.UPDATE_QUIZ_RECORD,
    quizId,
});

export const convertMeaningToQuizRecord = meaning => ({
    type: actionTypes.CONVERT_MEANING_TO_QUIZ_RECORD,
    meaning,
});

export const convertMeaningToQuizRecordStart = () => ({
    type: actionTypes.CONVERT_MEANING_TO_QUIZ_RECORD_START,
});

export const convertMeaningToQuizRecordFailure = () => ({
    type: actionTypes.CONVERT_MEANING_TO_QUIZ_RECORD_FAILURE,
});

export const convertMeaningToQuizRecordSuccess = () => ({
    type: actionTypes.CONVERT_MEANING_TO_QUIZ_RECORD_SUCCESS,
});

export const storeMeaningToConvertToQuizRecord = (meaning) => ({
    type: actionTypes.STORE_MEANING_TO_CONVERT_TO_QUIZ_RECORD,
    meaning,
});
