import * as actionTypes from '../actionTypes/quizzes';
import { SIGN_OUT } from '../actionTypes';

const initialState = {
    loading: false,
    quizzes: [],
    selectedQuizIndex: -1,
    loadedQuiz: null,
};

const fetchAllQuizzesStart = state => ({
    ...state,
    loading: true,
    quizzes: [],
    selectedQuizIndex: -1,
    loadedQuiz: null,
});

const fetchAllQuizzesSuccess = (state, action) => ({
    ...state,
    loading: false,
    quizzes: action.quizzes,
});

const fetchAllQuizzesFailure = state => ({
    ...state,
    loading: false,
});

const selectQuiz = (state, action) => ({
    ...state,
    selectedQuizIndex: action.index,
});

const signOut = state => ({
    ...state,
    quizzes: [],
    selectedQuizIndex: -1,
    loadedQuiz: null,
});

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.FETCH_ALL_QUIZZES_START: return fetchAllQuizzesStart(state, action);
        case actionTypes.FETCH_ALL_QUIZZES_SUCCESS: return fetchAllQuizzesSuccess(state, action);
        case actionTypes.FETCH_ALL_QUIZZES_FAILURE: return fetchAllQuizzesFailure(state, action);
        case actionTypes.SELECT_QUIZ: return selectQuiz(state, action);
        case SIGN_OUT: return signOut(state);
        default: return state;
    }
}

export default reducer;
