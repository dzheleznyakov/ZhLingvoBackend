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

export const createWord = (dictionaryId, mainForm) => ({
    type: actionTypes.CREATE_WORD,
    dictionaryId,
    mainForm,
});

export const deleteSelectedWord = dictionaryId => ({
    type: actionTypes.DELETE_SELECTED_WORD,
    dictionaryId,
});

export const setWordEditing = isEditing => ({
    type: actionTypes.SET_WORD_EDITING,
    isEditing,
});

export const updateWordMainForm = updatedMainForm => ({
    type: actionTypes.UPDATE_WORD_MAIN_FORM,
    updatedMainForm,
});

export const updateWordElement = (path, value) => ({
    type: actionTypes.UPDATE_WORD_ELEMENT,
    path,
    value,
});

export const updateWord = dictionaryId => ({
    type: actionTypes.UPDATE_WORD,
    dictionaryId,
});

export const shouldShowWordEditModal = show => ({
    type: actionTypes.SHOULD_SHOW_WORD_EDIT_MODAL,
    show,
});

export const setWordEditModalType = (modalType, path) => ({
    type: actionTypes.SET_WORD_EDIT_MODAL_TYPE,
    modalType,
    path,
});
