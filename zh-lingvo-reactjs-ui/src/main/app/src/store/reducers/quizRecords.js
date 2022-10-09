import * as actionTypes from '../actionTypes/quizRecords';
import { SIGN_OUT } from '../actionTypes';

const initialState = {
    quizRecordsOverviews: [],
    selectedQuizRecordIndex: -1,
    loading: false,
};

const fetchQuizRecordsSuccess = (state, action) => ({
    ...state,
    quizRecordsOverviews: action.overviews,
});

const selectQuizRecord = (state, action) => ({
    ...state,
    selectedQuizRecordIndex: action.index,
})

const signOut = () => ({
    ...initialState
});

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.FETCH_QUIZ_RECORDS_SUCCESS: return fetchQuizRecordsSuccess(state, action);
        case actionTypes.SELECT_QUIZ_RECORD: return selectQuizRecord(state, action);
        case SIGN_OUT: return signOut();
        default: return state;
    }
};

export default reducer;
