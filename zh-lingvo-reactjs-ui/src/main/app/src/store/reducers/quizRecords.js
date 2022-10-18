import * as actionTypes from '../actionTypes/quizRecords';
import { SIGN_OUT } from '../actionTypes';

const initialState = {
    quizRecordsOverviews: [],
    selectedQuizRecordIndex: -1,
    loading: false,
    loadedQuizRecord: null,
};

const fetchQuizRecordsSuccess = (state, action) => ({
    ...state,
    quizRecordsOverviews: action.overviews,
});

const selectQuizRecord = (state, action) => ({
    ...state,
    selectedQuizRecordIndex: action.index,
});

const fetchQuizRecordStart = state => ({
    ...state,
    loading: true,
});

const fetchQuizRecordSuccess = (state, action) =>({
    ...state,
    loading: false,
    loadedQuizRecord: action.quizRecord,
});

const fetchQuizRecordFailure = state => ({
    ...state,
    loading: false,
});

const signOut = () => ({
    ...initialState
});

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.FETCH_QUIZ_RECORDS_SUCCESS: return fetchQuizRecordsSuccess(state, action);
        case actionTypes.SELECT_QUIZ_RECORD: return selectQuizRecord(state, action);
        case actionTypes.FETCH_QUIZ_RECORD_START: return fetchQuizRecordStart(state, action);
        case actionTypes.FETCH_QUIZ_RECORD_SUCCESS: return fetchQuizRecordSuccess(state, action);
        case actionTypes.FETCH_QUIZ_RECORD_FAILURE: return fetchQuizRecordFailure(state, action);
        case SIGN_OUT: return signOut();
        default: return state;
    }
};

export default reducer;
