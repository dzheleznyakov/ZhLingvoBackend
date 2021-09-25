import _ from 'lodash';
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
        yield call(axios.post, `/words/dictionary/${dictionaryId}`, word);
    } catch (error) {
        yield put(actions.addError(
            error.response.data,
            `Error while createing word [${mainForm}] for dictionary [${dictionaryId}]`));
        return;
    }

    yield put(actions.fetchWordsList(dictionaryId));
    const { wordsList } = yield take(actionTypes.FETCH_WORDS_LIST_SUCCESS);
    const selectedWordIndex = wordsList.indexOf(mainForm);
    const currentBreadcrumbs = yield select(state => state.control.breadcrumbs);
    yield all([
        yield put(actions.selectWord(selectedWordIndex)),
        yield put(actions.navigateTo(`/dictionaries/${dictionaryId}/${mainForm}`)),
        yield put(actions.setBreadcrumbs([
                currentBreadcrumbs[0],
                currentBreadcrumbs[1],
                mainForm,
        ])),
        yield put(actions.fetchWord(dictionaryId, mainForm)),
    ]);
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

export function* updateWordSaga(action) {
    const word = _.cloneDeep(yield select(selectors.updatedWordSelector));
    if (!word) {
        yield put(actions.setWordEditing(false));
        return;
    }

    const { dictionaryId } = action;
    const mainForm = word[0].mainForm;
    try {
        for (let i = 0, l = word.length; i < l; ++i)
            yield call(axios.put, `/words/dictionary/${dictionaryId}/${word[i].id}`, word[i]);
    } catch (error) {
        yield put(actions.addError(
            error.response.data,
            `Error while updating word [${mainForm}]`));
        return;
    }

    yield put(actions.setWordEditing(false));
    yield put(actions.fetchWordsList(dictionaryId));
    yield put(actions.fetchWord(dictionaryId, mainForm));
    const { wordsList } = yield take(actionTypes.FETCH_WORDS_LIST_SUCCESS);
    const selectedWordIndex = wordsList.indexOf(mainForm);
    all([
        yield put(actions.selectWord(selectedWordIndex)),
        yield put(actions.navigateTo(`/dictionaries/${dictionaryId}/${wordsList[selectedWordIndex]}`)),
    ]);
}

export function* fetchPosSaga(action) {
    const { lang } = action;
    try {
        const { data } = yield call(axios.get, `/pos/${lang}`);
        yield put(actions.setPos(data));
    } catch (error) {
        yield call(actions.addError(
            error.response.data,
            `Error while fetching parts of speech for [${lang}] language`));
        yield put(actions.setPos([]));
    }
}
