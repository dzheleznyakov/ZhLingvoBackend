import * as actionTypes from '../actionTypes/words';

export const fetchWordsList = dictionaryId => ({
    type: actionTypes.FETCH_WORDS_LIST,
    dictionaryId,
});

export const fetchWordsListSuccess = wordsList => ({
    type: actionTypes.FETCH_WORDS_LIST_SUCCESS,
    wordsList,
});

export const fetchWord = (dictionaryId, wordMainForm) => ({
    type: actionTypes.FETCH_WORD,
    dictionaryId,
    wordMainForm,
});

export const fetchWordStart = () => ({
    type: actionTypes.FETCH_WORD_START,
});

export const fetchWordSuccess = word => ({
    type: actionTypes.FETCH_WORD_SUCCESS,
    word,
});

export const fetchWordFailure = () => ({
    type: actionTypes.FETCH_WORD_FAILURE,
});

export const selectWord = index => ({
    type: actionTypes.SELECT_WORD,
    index,
});
