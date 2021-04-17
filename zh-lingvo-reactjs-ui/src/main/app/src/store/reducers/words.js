import _ from 'lodash';

import * as actionTypes from '../actionTypes/words';
import { SIGN_OUT } from '../actionTypes/auth';

const initialState = {
    wordsList: [],
    selectedWordIndex: -1,
    loading: false,
    loadedWord: null,
    isEditing: false,
    updatedWord: null,
};

const fetchWordsListSuccess = (state, action) => ({
    ...state,
    wordsList: action.wordsList,
    selectedWordIndex: -1,
    loading: false,
    updatedWord: null,
});

const fetchWordStart = (state) => ({
    ...state,
    loading: true,
    updatedWord: null,
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
    updatedWord: null,
});

const setWordEditing = (state, action) => ({
    ...state,
    isEditing: action.isEditing,
    updatedWord: action.isEditing ? state.updatedWord : null,
});

const updateWord = (state, aciton, decorator) => {
    if (!state.loadedWord)
        return state;
    const newUpdatedWord = state.updatedWord
        ? _.cloneDeep(state.updatedWord)
        : _.cloneDeep(state.loadedWord);
    decorator(newUpdatedWord, aciton);
    return { ...state, updatedWord: newUpdatedWord };
};

const updateWordMainForm = (state, action) => updateWord(state, action,
    (nuw, action) => nuw.forEach(uw => uw.mainForm = action.updatedMainForm));

const updateWordElement = (state, action) => updateWord(state, action,
    (nuw, action) => _.set(nuw, action.path, action.value));

const signOut = state => ({
    ...state,
    wordsList: [],
    selectedWordIndex: -1,
    loading: false,
    loadedWord: null,
    isEditing: false,
    updatedWord: null,
});

export default (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.FETCH_WORDS_LIST_SUCCESS: return fetchWordsListSuccess(state, action);
        case actionTypes.FETCH_WORD_START: return fetchWordStart(state, action);
        case actionTypes.FETCH_WORD_SUCCESS: return fetchWordSuccess(state, action);
        case actionTypes.FETCH_WORD_FAILURE: return fetchWordFailure(state, action);
        case actionTypes.SELECT_WORD: return selectWord(state, action);
        case actionTypes.SET_WORD_EDITING: return setWordEditing(state, action);
        case actionTypes.UPDATE_WORD_MAIN_FORM: return updateWordMainForm(state, action);
        case actionTypes.UPDATE_WORD_ELEMENT: return updateWordElement(state, action);
        case SIGN_OUT: return signOut(state);
        default: return state;
    }
};
