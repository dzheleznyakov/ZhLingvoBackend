import * as actionTypes from '../actionTypes/dictionaries';
import { SIGN_OUT } from '../actionTypes/auth';

const initialState = {
    loading: false,
    languages: [],
    dictionaries: [],
    selectedDictionaryIndex: -1,
    loadedDictionary: null,
};

const fetchAllDictionariesStart = state => ({
    ...state,
    loading: true,
    dictionaries: [],
    selectedDictionaryIndex: -1,
    loadedDictionary: null,
});

const fetchAllDictionariesSuccess = (state, action) => ({
    ...state,
    loading: false,
    dictionaries: action.dictionaries,
});

const fetchAllDictionariesFailure = (state) => ({
    ...state,
    loading: false
});

const fetchDictionaryStart = (state) => ({
    ...state,
    loadedDictionary: null,
    loading: true,
});

const fetchDictionarySuccess = (state, action) => ({
    ...state,
    loadedDictionary: action.dictionary,
    loading: false,
});

const fetchDictionaryFailure = (state) => ({
    ...state,
    loading: false,
});

const selectDictionary = (state, action) => ({
    ...state,
    selectedDictionaryIndex: action.selectedDictionaryIndex,
});

const unselectDictionary = (state, action) => {
    const { selectedDictionaryIndex } = state;
    const { dictionaryIndex } = action;
    const updatedSelectedDictionaryIndex = selectedDictionaryIndex === dictionaryIndex ? -1 : selectedDictionaryIndex;
    return { ...state, selectedDictionaryIndex: updatedSelectedDictionaryIndex };
};

const fetchAllLanguages = (state, action) => ({
    ...state,
    languages: action.languages || [],
});

const signOut = state => ({
    ...state,
    dictionaries: [],
    selectedDictionaryIndex: -1,
});

export default (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.FETCH_ALL_DICTIONARIES_START: return fetchAllDictionariesStart(state, action);
        case actionTypes.FETCH_ALL_DICTIONARIES_SUCCESS: return fetchAllDictionariesSuccess(state, action);
        case actionTypes.FETCH_ALL_DICTIONARIES_FAILURE: return fetchAllDictionariesFailure(state, action);
        case actionTypes.FETCH_DICTIONARY_START: return fetchDictionaryStart(state, action);
        case actionTypes.FETCH_DICTIONARY_SUCCESS: return fetchDictionarySuccess(state, action);
        case actionTypes.FETCH_DICTIONARY_FAILURE: return fetchDictionaryFailure(state, action);
        case actionTypes.SELECT_DICTIONARY: return selectDictionary(state, action);
        case actionTypes.UNSELECT_DICTIONARY: return unselectDictionary(state, action);
        case actionTypes.FETCH_ALL_LANGUAGES_SUCCESS: return fetchAllLanguages(state, action);
        case SIGN_OUT: return signOut(state);
        default: return state;
    }
};