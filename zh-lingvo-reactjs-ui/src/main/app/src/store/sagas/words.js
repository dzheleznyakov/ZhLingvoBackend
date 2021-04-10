import { put, call } from 'redux-saga/effects';

import axios from '../../axios-api';
import * as actions from '../actions';

export function* fetchWordsListSaga(action) {
    const { dictionaryId } = action;
    try {
        const { data } = yield call(axios.get, `/words/dictionary/${dictionaryId}`);
        yield put(actions.fetchWordsListSuccess(data));
    } catch (error) {
        yield put(actions.addError(
            error.response.data, 
            `Error while fetching words list for dictionary [${dictionaryId}]`));
    }
}

export function* fetchWordSaga(action) {
    const { dictionaryId, wordMainForm } = action;
    yield put(actions.fetchWordStart());
    try {
        const { data } = yield call(axios.get, `/words/dictionary/${dictionaryId}/mainForm/${wordMainForm}`);
        yield put(actions.fetchWordSuccess(data));
    } catch (error) {
        yield put(actions.fetchWordFailure());
        yield put(actions.addError(
            error.response.data, 
            `Error while fetching words [${wordMainForm}] for dictionary [${dictionaryId}]`));
    }
}

export function* createWordSaga(action) {
    const { dictionaryId, mainForm } = action;
    try {
        const word = { mainForm }
        const { data } = yield call(axios.post, `/words/dictionary/${dictionaryId}`, word);
        yield put(actions.createWordSuccess(data));
    } catch (error) {
        yield put(actions.addError(
            error.response.data,
            `Error while createing word [${mainForm}] for dictionary [${dictionaryId}]`));
    }
}
