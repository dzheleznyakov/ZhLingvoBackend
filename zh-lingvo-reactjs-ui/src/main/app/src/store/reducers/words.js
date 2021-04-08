import * as actionTypes from '../actionTypes/words';
import { SIGN_OUT } from '../actionTypes/auth';

const initialState = {
    wordsList: [],
    selectedWordIndex: -1,
};

const fetchWordsListSuccess = (state, action) => ({
    ...state,
    wordsList: action.wordsList,
    selectedWordIndex: -1,
});

const selectWord = (state, action) => ({
    ...state,
    selectedWordIndex: action.index,
});

const signOut = state => ({
    ...state,
    wordsList: [],
    selectedWordIndex: -1,
});

export default (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.FETCH_WORDS_LIST_SUCCESS: return fetchWordsListSuccess(state, action);
        case actionTypes.SELECT_WORD: return selectWord(state, action);
        case SIGN_OUT: return signOut(state);
        default: return state;
    }
};
