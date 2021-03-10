import * as actionTypes from '../actionTypes/dictionaries';

const initialState = {
    loading: false,
    error: null,
    languages: [],
    dictionaries: [],
    selectedDictionaryIndex: -1,
};

const fetchAllDictionariesStart = state => ({
    ...state,
    loading: true,
    error: null,
    dictionaries: [],
    selectedDictionaryIndex: -1,
});

const fetchAllDictionariesSuccess = (state, action) => ({
    ...state,
    loading: false,
    error: null,
    dictionaries: action.dictionaries,
});

const createDictionarySuccess = (state, action) => ({
    ...state,
    dictionaries: state.dictionaries.concat(action.dictionary),
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

const fetchAllLanguages = (state, action) => {
    console.log(state);
    console.log(action);
    return {
        ...state,
        languages: action.languages || [],
}};

export default (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.FETCH_ALL_DICTIONARIES_START: return fetchAllDictionariesStart(state, action);
        case actionTypes.FETCH_ALL_DICTIONARIES_SUCCESS: return fetchAllDictionariesSuccess(state, action);
        case actionTypes.CREATE_DICTIONARY_SUCCESS: return createDictionarySuccess(state, action);
        case actionTypes.SELECT_DICTIONARY: return selectDictionary(state, action);
        case actionTypes.UNSELECT_DICTIONARY: return unselectDictionary(state, action);
        case actionTypes.FETCH_ALL_LANGUAGES_SUCCESS: return fetchAllLanguages(state, action);
        default: return state;
    }
};