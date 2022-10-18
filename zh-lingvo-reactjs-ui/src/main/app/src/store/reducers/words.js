import _ from 'lodash';

import * as actionTypes from '../actionTypes/words';
import { SIGN_OUT } from '../actionTypes/auth';
import { NONE } from '../../static/constants/wordEditModalTypes';

const initialState = {
    wordsList: [],
    selectedWordIndex: -1,
    loading: false,
    loadedWord: null,
    isEditing: false,
    updatedWord: null,
    showWordEditModal: false,
    wordEditModalType: NONE,
    wordEditPath: [],
    posList: [],
};

const fetchWordsListSuccess = (state, action) => ({
    ...state,
    wordsList: action.wordsList,
    selectedWordIndex: -1,
    loading: false,
    updatedWord: null,
});

const fetchWordStart = state => ({
    ...state,
    loading: true,
});



const fetchWordSuccess = (state, action) => {
    const { updatedWord } = state;
    const { word } = action;
    const newUpdatedWord = updatedWord
        ? _.cloneDeep(word)
        : null;
    return {
        ...state,
        loading: false,
        loadedWord: word,
        updatedWord: newUpdatedWord,
    };
};

const fetchWordFailure = state => ({
    ...state,
    loading: false,
});

const selectWord = (state, action) => ({
    ...state,
    selectedWordIndex: action.index,
});

const setWordEditing = (state, action) => {
    const { loadedWord } = state;
    const { isEditing } = action;
    const newUpdatedWord = isEditing ? _.cloneDeep(loadedWord) : null;
    return {
        ...state,
        isEditing,
        updatedWord: newUpdatedWord,
        shouldShowWordEditModal: false,
        wordEditModalType: NONE,
        wordEditPath: [],
    };
};

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

const shouldShowWordEditModal = (state, action) => ({
    ...state,
    shouldShowWordEditModal: action.show,
});

const setWordEditModalType = (state, action) => ({
    ...state,
    wordEditModalType: action.modalType,
    wordEditPath: action.path,
});

const setPos = (state, action) => ({
    ...state,
    posList: action.posList,
});

const signOut = () => ({
    ...initialState
});

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.FETCH_WORDS_LIST_SUCCESS: return fetchWordsListSuccess(state, action);
        case actionTypes.FETCH_WORD_START: return fetchWordStart(state, action);
        case actionTypes.FETCH_WORD_SUCCESS: return fetchWordSuccess(state, action);
        case actionTypes.FETCH_WORD_FAILURE: return fetchWordFailure(state, action);
        case actionTypes.SELECT_WORD: return selectWord(state, action);
        case actionTypes.SET_WORD_EDITING: return setWordEditing(state, action);
        case actionTypes.UPDATE_WORD_MAIN_FORM: return updateWordMainForm(state, action);
        case actionTypes.UPDATE_WORD_ELEMENT: return updateWordElement(state, action);
        case actionTypes.SHOULD_SHOW_WORD_EDIT_MODAL: return shouldShowWordEditModal(state, action);
        case actionTypes.SET_WORD_EDIT_MODAL_TYPE: return setWordEditModalType(state, action);
        case actionTypes.SET_POS: return setPos(state, action);
        case SIGN_OUT: return signOut(state);
        default: return state;
    }
};

export default reducer;
