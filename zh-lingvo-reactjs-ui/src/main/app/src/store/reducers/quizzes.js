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
    meaningToQuizRecord: {
        quizzes: [],
        loadingQuizzes: false,
        targetQuiz: {},
    },
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

const updateMeaningToQuizRecord = (state, action, decorator) => {
    const updatedMeaningToQuizRecord = {
        ...state.meaningToQuizRecord,
    };
    return {
        ...state,
        meaningToQuizRecord: decorator(updatedMeaningToQuizRecord, action),
    };
};

const fetchAllQuizzesByLanguageStart = state => updateMeaningToQuizRecord(state, null,
  umtqr => {
    umtqr.loadingQuizzes = true;
    return umtqr;
  },
);

const fetchAllQuizzesByLanguageSuccess = (state, action) => updateMeaningToQuizRecord(state, action,
    (umtqr, ac) => {
        umtqr.loadingQuizzes = false;
        umtqr.quizzes = ac.quizzes;
        return umtqr;
    },
);

const fetchAllQuizzesByLanguageFailure = state => updateMeaningToQuizRecord(state, null, 
    umtqr => {
        umtqr.loadingQuizzes = false;
        umtqr.quizzes = [];
        return umtqr;
    }
);

const createQuizForMeaningToQuizRecordSuccess = (state, action) => updateMeaningToQuizRecord(state, action,
    (umtqr, ac) => {
        umtqr.targetQuiz = ac.quiz;
        return umtqr;
    },
);

const signOut = state => ({
    ...state,
    quizzes: [],
    selectedQuizIndex: -1,
    loadedQuiz: null,
    meaningToQuizRecord: { ...initialState.meaningToQuizRecord },
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
        case actionTypes.FETCH_ALL_QUIZZES_BY_LANGUAGE_START: return fetchAllQuizzesByLanguageStart(state, action);
        case actionTypes.FETCH_ALL_QUIZZES_BY_LANGUAGE_SUCCESS: return fetchAllQuizzesByLanguageSuccess(state, action);
        case actionTypes.FETCH_ALL_QUIZZES_BY_LANGUAGE_FAILURE: return fetchAllQuizzesByLanguageFailure(state, action);
        case actionTypes.CREATE_QUIZ_FOR_MEANING_TO_QUIZ_RECORD_SUCCESS: return createQuizForMeaningToQuizRecordSuccess(state, action);
        case SIGN_OUT: return signOut(state);
        default: return state;
    }
}

export default reducer;
