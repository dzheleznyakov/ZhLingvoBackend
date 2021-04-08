import * as actionTypes from '../actionTypes/dictionaries';

export const fetchAllDictionaries = () => ({
    type: actionTypes.FETCH_ALL_DICTIONARIES,
});

export const fetchAllDictionariesStart = () => ({
    type: actionTypes.FETCH_ALL_DICTIONARIES_START,
});

export const fetchAllDictionariesSuccess = dictionaries => ({
    type: actionTypes.FETCH_ALL_DICTIONARIES_SUCCESS,
    dictionaries,
});

export const fetchAllDictionariesFailure = () => ({
    type: actionTypes.FETCH_ALL_DICTIONARIES_FAILURE,
});

export const fetchDictionary = id => ({
    type: actionTypes.FETCH_DICTIONARY,
    id,
});

export const fetchDictionaryStart = () => ({
    type: actionTypes.FETCH_DICTIONARY_START,
});

export const fetchDictionarySuccess = dictionary => ({
    type: actionTypes.FETCH_DICTIONARY_SUCCESS,
    dictionary,
});

export const fetchDictionaryFailure = () => ({
    type: actionTypes.FETCH_DICTIONARY_FAILURE,
});

export const createDictionary = (name, language) => ({
    type: actionTypes.CREATE_DICTIONARY,
    name,
    language,
});

export const updateDictionary = (name) => ({
    type: actionTypes.UPDATE_DICTIONARY,
    name,
});

export const deleteDictionary = id => ({
    type: actionTypes.DELETE_DICTIONARY,
    id,
});

export const selectDictionary = selectedDictionaryIndex => ({
    type: actionTypes.SELECT_DICTIONARY,
    selectedDictionaryIndex,
});

export const unselectDictionary = dictionaryIndex => ({
    type: actionTypes.UNSELECT_DICTIONARY,
    dictionaryIndex,
});

export const fetchAllLanguages = () => ({
    type: actionTypes.FETCH_ALL_LANGUAGES,
});

export const fetchAllLanguagesSuccess = languages => ({
    type: actionTypes.FETCH_ALL_LANGUAGES_SUCCESS,
    languages,
});
