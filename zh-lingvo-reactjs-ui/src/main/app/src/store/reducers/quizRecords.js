import _ from 'lodash';

import * as actionTypes from '../actionTypes/quizRecords';
import { SIGN_OUT } from '../actionTypes';
import { NONE } from '../../static/constants/quizRecordEditModalTypes';

const initialState = {
    quizRecordsOverviews: [],
    selectedQuizRecordIndex: -1,
    loading: false,
    loadedQuizRecord: null,
    isEditing: false,
    updatedQuizRecord: null,
    showQuizRecordEditModal: false,
    quizRecordEditModalType: NONE,
    quizRecordEditPath: [],
};

const fetchQuizRecordsSuccess = (state, action) => ({
    ...state,
    quizRecordsOverviews: action.overviews,
});

const selectQuizRecord = (state, action) => ({
    ...state,
    selectedQuizRecordIndex: action.index,
});

const fetchQuizRecordStart = state => ({
    ...state,
    loading: true,
});

const fetchQuizRecordSuccess = (state, action) =>({
    ...state,
    loading: false,
    loadedQuizRecord: action.quizRecord,
});

const fetchQuizRecordFailure = state => ({
    ...state,
    loading: false,
});

const setQuizRectordEditing = (state, action) => {
    const { loadedQuizRecord } = state;
    const { isEditing } = action;
    const newUpdatedQuizRecord = isEditing 
        ? _.cloneDeep(loadedQuizRecord)
        : null;
    return {
        ...state,
        isEditing,
        updatedQuizRecord: newUpdatedQuizRecord,
        showQuizRecordEditModal: false,
        quizRecordEditModalType: NONE,
        quizRecordEditPath: [],
    };
};

const setQuizRecordEditModalType = (state, action) => ({
    ...state,
    quizRecordEditModalType: action.modalType,
    quizRecordEditPath: action.path,
});

const shouldShowQuizRecordEditModal = (state, action) => ({
    ...state,
    showQuizRecordEditModal: action.show,
});

const updateWord = (state, action, decorator) => {
    if (!state.loadedQuizRecord)
        return state;
    const newUpdatedQuizRecord = state.updatedQuizRecord
        ? _.cloneDeep(state.updatedQuizRecord)
        : _.cloneDeep(state.loadedQuizRecord);
    decorator(newUpdatedQuizRecord, action);
    return { ...state, updatedQuizRecord: newUpdatedQuizRecord };
}

const updateQuizRecordMainForm = (state, action) => updateWord(state, action,
    (nuw, action) => nuw.wordMainForm = action.updatedMainForm);

const signOut = () => ({
    ...initialState
});

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.FETCH_QUIZ_RECORDS_SUCCESS: return fetchQuizRecordsSuccess(state, action);
        case actionTypes.SELECT_QUIZ_RECORD: return selectQuizRecord(state, action);
        case actionTypes.FETCH_QUIZ_RECORD_START: return fetchQuizRecordStart(state, action);
        case actionTypes.FETCH_QUIZ_RECORD_SUCCESS: return fetchQuizRecordSuccess(state, action);
        case actionTypes.FETCH_QUIZ_RECORD_FAILURE: return fetchQuizRecordFailure(state, action);
        case actionTypes.SET_QUIZ_RECORD_EDITING: return setQuizRectordEditing(state, action);
        case actionTypes.SET_QUIZ_RECORD_EDIT_MODAL_TYPE: return setQuizRecordEditModalType(state, action);
        case actionTypes.SHOULD_SHOW_QUIZ_RECORD_EDIT_MODAL: return shouldShowQuizRecordEditModal(state, action);
        case actionTypes.UPDATE_QUIZ_RECORD_MAIN_FORM: return updateQuizRecordMainForm(state, action);
        case SIGN_OUT: return signOut();
        default: return state;
    }
};

export default reducer;
