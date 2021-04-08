import * as actionTypes from '../actionTypes/words';

export const fetchWordsList = dictionaryId => ({
    type: actionTypes.FETCH_WORDS_LIST,
    dictionaryId,
});

export const fetchWordsListSuccess = wordsList => ({
    type: actionTypes.FETCH_WORDS_LIST_SUCCESS,
    wordsList,
});

export const selectWord = index => ({
    type: actionTypes.SELECT_WORD,
    index,
});
