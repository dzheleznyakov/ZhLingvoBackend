import * as actionTypes from '../actionTypes/words';
import { SIGN_OUT } from '../actionTypes/auth';

const initialState = {
    wordsList: [],
    selectedWordIndex: -1,
    loading: false,
    loadedWord: null,
};

const fetchWordsListSuccess = (state, action) => ({
    ...state,
    wordsList: action.wordsList,
    selectedWordIndex: -1,
    loading: false,
});

const fetchWordStart = (state) => ({
    ...state,
    loading: true,
});

const fetchWordSuccess = (state, action) => ({
    ...state,
    loading: false,
    loadedWord: action.word,
});

const fetchWordFailure = (state) => ({
    ...state,
    loading: false,
});

const selectWord = (state, action) => ({
    ...state,
    selectedWordIndex: action.index,
});

const createWordSuccess = (state, action) => {
    const updatedWordsList = [action.word.mainForm].concat(state.wordsList);
    updatedWordsList.sort();
    return { 
        ...state, 
        wordsList: updatedWordsList,
    };
};

const signOut = state => ({
    ...state,
    wordsList: [],
    selectedWordIndex: -1,
    loading: false,
    loadedWord: null,
});

export default (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.FETCH_WORDS_LIST_SUCCESS: return fetchWordsListSuccess(state, action);
        case actionTypes.FETCH_WORD_START: return fetchWordStart(state, action);
        case actionTypes.FETCH_WORD_SUCCESS: return fetchWordSuccess(state, action);
        case actionTypes.FETCH_WORD_FAILURE: return fetchWordFailure(state, action);
        case actionTypes.SELECT_WORD: return selectWord(state, action);
        case actionTypes.CREATE_WORD_SUCCESS: return createWordSuccess(state, action);
        case SIGN_OUT: return signOut(state);
        default: return state;
    }
};
