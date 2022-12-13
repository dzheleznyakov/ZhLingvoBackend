import { all, call, put, select, take } from "redux-saga/effects";
import _ from 'lodash';

import api from '../../axios-api';
import * as actions from '../actions';
import * as selectors from '../selectors';
import * as actionTypes from '../actionTypes';

export function* fetchQuizRecords(action) {
    const { quizId } = action;

    try {
        const { data } = yield call(
            api.get, 
            `/quizzes/${quizId}/records/overviews`);
        yield put(actions.fetchQuizRecordsSuccess(data));
    } catch (error) {
        yield put(actions.addError(
            error.response.data,
            `Error while fetching quiz records for quiz [${quizId}]`));
    }
}

export function* createQuizSaga(action) {
    const { quizId, wordMainForm, pos } = action;
    try { 
        const newRecord = { wordMainForm, pos };
        yield call(api.post, `/quizzes/${quizId}/records`, newRecord);
    } catch (error) {
        yield put(actions.addError(
            error.response.data,
            `Error while creating quiz record [${wordMainForm}] for quiz [${quizId}]`));
        return;
    }

    yield put(actions.fetchQuizRecords(quizId));
    const { overviews } = yield take(actionTypes.FETCH_QUIZ_RECORDS_SUCCESS);
    const selectedQuizRecordIndex = overviews
        .findLastIndex(overview => overview.wordMainForm === wordMainForm);
    const selectedQuizRecordId = overviews[selectedQuizRecordIndex].id;
    yield all([
        put(actions.selectQuizRecord(selectedQuizRecordIndex)),
        put(actions.navigateTo(`/tutor/quiz/${quizId}/${selectedQuizRecordId}`)),
        put(actions.fetchQuizRecord(quizId, selectedQuizRecordId)),
    ]);
}

export function* fetchQuizRecordSaga(action) {
    const { quizId, recordId } = action;
    yield put(actions.fetchQuizRecordStart());
    try {
        const { data: quizRecord } = yield call(api.get, `/quizzes/${quizId}/records/${recordId}`);
        yield put(actions.fetchQuizRecordSuccess(quizRecord));
    } catch (error) {
        yield all([
            put(actions.fetchQuizRecordFailure()),
            put(actions.addError(
                error.response.data,
                `Error while fetching quiz record [${recordId}] for quiz [${quizId}]`,
            ))
        ]);
    }
}

export function* deleteQuizRecordSaga(action) {
    const { quizId, recordId } = action;

    if (Number.isNaN(quizId) || Number.isNaN(recordId))
        return;

    try {
        yield call(api.delete, `/quizzes/${quizId}/records/${recordId}`);
    } catch (error) {
        yield put(actions.addError(
            error.response.data,
            `Error while deleting quiz record [${recordId}] for quiz [${quizId}]`));
        return;
    }

    const selectedQuizRecordIndex = yield select(selectors.selectedQuizRecordIndexSelector);
    yield put(actions.fetchQuizRecords(quizId));
    const { overviews } = yield take(actionTypes.FETCH_QUIZ_RECORDS_SUCCESS);
    
    if (overviews.length === 0) {
        yield put(actions.selectQuizRecord(-1));
        yield put(actions.navigateTo(`/tutor/quiz/${quizId}`));
        return;
    }

    let newSelectedQuizRecordIndex = selectedQuizRecordIndex;
    if (newSelectedQuizRecordIndex >= overviews.length)
        newSelectedQuizRecordIndex = overviews.length - 1;

    const newSelectedQuizRecordId = overviews[newSelectedQuizRecordIndex].id;
    yield all([
        put(actions.selectQuizRecord(newSelectedQuizRecordIndex)),
        put(actions.navigateTo(`/tutor/quiz/${quizId}/${newSelectedQuizRecordId}`)),
        put(actions.fetchQuizRecord(quizId, newSelectedQuizRecordId)),
    ]);
}

export function* updateQuizRecordSaga(action) {
    const record = _.cloneDeep(
        yield select(selectors.updatedQuizRecordSelector));
    if (!record) {
        yield put(actions.setQuizRecordEditing(false));
        return;
    }

    const { quizId } = action;
    try {
        yield call(api.put, `/quizzes/${quizId}/records`, record);
    } catch (error) {
        yield put(actions.addError(
            error.response.data,
            `Error while update quiz record [${record.id}]`));
        return;
    }

    yield put(actions.setQuizRecordEditing(false));
    yield put(actions.fetchQuizRecords(quizId));
    yield put(actions.fetchQuizRecord(quizId, record.id));
    const { overviews: recordList } = yield take(actionTypes.FETCH_QUIZ_RECORDS_SUCCESS);
    const selectedQuizRecordIndex = recordList.findIndex(rec => rec.id === record.id);
    yield put(actions.selectQuizRecord(selectedQuizRecordIndex));
    yield put(actions.navigateTo(`/tutor/quiz/${quizId}/${record.id}`));
}
