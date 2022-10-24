import * as actionTypes from '../actionTypes/quizzes';
import { SIGN_OUT } from '../actionTypes';

const initialState = {
    loading: false,
    quizzes: [],
    selectedQuizIndex: -1,
    loadedQuiz: null,
    matchingRegimes: [],
    quizRegimes: [],
    settings: {},
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

const fetchQuizStart = state => ({
    ...state,
    loadedQuiz: null,
    loading: true,
});

const fetchQuizSuccess = (state, action) => ({
    ...state,
    loadedQuiz: action.quiz,
    loading: false,
});

const fetchQuizFailure = state => ({
    ...state,
    loading: false,
});

const fetchMatchingRegimesSuccess = (state, action) => ({
    ...state,
    matchingRegimes: action.matchingRegimes,
});

const fetchQuizRegimesSuccess = (state, action) => ({
    ...state,
    quizRegimes: action.quizRegimes,
});

const fetchQuizSettingsSuccess = (state, action) => {
    const { settings: currentSettings } = state;
    const { quizId, settings: newSettingsEntry } = action;
    const numberOfEntries = Object.keys(currentSettings).length;
    const updatedSettings = numberOfEntries < 5
        ? { ...currentSettings, [quizId]: newSettingsEntry }
        : { [quizId]: newSettingsEntry };
    return { ...state, settings: updatedSettings };
};

const updateQuizSettingsSuccess = (state, action) => {
    const { settings: currentSettings } = state;
    const { quizId, settings: updatedQuizSettings } = action;
    if (currentSettings[quizId]) {
        const updatedSettings = { ...currentSettings, [quizId]: updatedQuizSettings };
        return { ...state, settings: updatedSettings };
    }
    return fetchQuizSettingsSuccess(state, action);
};

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
        case actionTypes.FETCH_QUIZ_START: return fetchQuizStart(state, action);
        case actionTypes.FETCH_QUIZ_SUCCESS: return fetchQuizSuccess(state, action);
        case actionTypes.FETCH_QUIZ_FAIURE: return fetchQuizFailure(state, action);
        case actionTypes.FETCH_MATCHING_REGIMES_SUCCESS: return fetchMatchingRegimesSuccess(state, action);
        case actionTypes.FETCH_QUIZ_REGIMES_SUCCESS: return fetchQuizRegimesSuccess(state, action);
        case actionTypes.FETCH_QUIZ_SETTINGS_SUCCESS: return fetchQuizSettingsSuccess(state, action);
        case actionTypes.UPDATE_QUIZ_SETTINGS_SUCCESS: return updateQuizSettingsSuccess(state, action); 
        case SIGN_OUT: return signOut(state);
        default: return state;
    }
}

export default reducer;
