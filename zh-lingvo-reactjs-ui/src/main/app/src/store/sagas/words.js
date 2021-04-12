import { put, call, select, take, all } from 'redux-saga/effects';

import axios from '../../axios-api';
import * as actions from '../actions';
import * as selectors from '../selectors';
import * as actionTypes from '../actionTypes';

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
        yield put(actions.navigateTo(`/dictionaries/${dictionaryId}/${mainForm}`));
        const currentBreadcrumbs = yield select(state => state.control.breadcrumbs);
        yield put(actions.setBreadcrumbs([
            currentBreadcrumbs[0],
            currentBreadcrumbs[1],
            mainForm,
        ]));
    } catch (error) {
        yield put(actions.addError(
            error.response.data,
            `Error while createing word [${mainForm}] for dictionary [${dictionaryId}]`));
    }
}

export function* deleteSelectedWordSaga(action) {
    const { dictionaryId } = action;
    const word = yield select(selectors.loadedWordSelector);
    if (!word || !word.length)
        return;
    
    try {
        for (let i = 0, l = word.length; i < l; ++i)
            yield call(axios.delete, `/words/${word[i].id}`)
    } catch (error) {
        yield put(actions.addError(
            error.response.data,
            `Error while deleting word [${word[0].mainForm}]`));
        return;
    }

    const selectedWordIndex = yield select(selectors.selectedWordIndexSelector);
    yield put(actions.fetchWordsList(dictionaryId));
    const { wordsList } = yield take(actionTypes.FETCH_WORDS_LIST_SUCCESS);
    const wLength = wordsList.length;
    if (wLength > selectedWordIndex)
        all([
            yield put(actions.selectWord(selectedWordIndex)),
            yield put(actions.navigateTo(`/dictionaries/${dictionaryId}/${wordsList[selectedWordIndex]}`)),
        ]);
    else if (wLength === 0 || selectedWordIndex < 0)
        yield put(actions.navigateTo(`/dictionaries/${dictionaryId}`));
    else
        all([
            yield put(actions.selectWord(wLength - 1)),
            yield put(actions.navigateTo(`/dictionaries/${dictionaryId}/${wordsList[wLength - 1]}`)),
        ]);
}
