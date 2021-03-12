import { call, put, select } from 'redux-saga/effects';

import axios from '../../axios-api';
import * as actions from '../actions';
import * as selectors from '../selectors';

export function* fetchAllDictionariesSaga() {
    yield put(actions.fetchAllDictionariesStart());

    const { data } = yield call(axios.get, '/dictionaries');
    yield put(actions.fetchAllDictionariesSuccess(data));
}

export function* fetchAllLanguagesSaga() {
    const languages = yield select(selectors.languagesSelector);
    if (languages.length)
        return;

    const { data } = yield call(axios.get, '/languages');
    yield put(actions.fetchAllLanguagesSuccess(data));
}

export function* createDictionarySaga(action) {
    const { name, language } = action;
    const newDictionary = { name, language}

    yield call(axios.post, '/dictionaries', newDictionary);
    yield put(actions.fetchAllDictionaries());
}

export function* updateDictionarySaga(action) {
    const { name } = action;
    const currentDictionary = yield select(selectors.currentDictionarySelector);
    const updatedDictionary = { ...currentDictionary, name };
    yield call(axios.put, '/dictionaries', updatedDictionary);
    yield put(actions.fetchAllDictionaries());
}

export function* deleteDictionarySaga(action) {
    const { id } = action;
    if (id < 0)
        return;

    const { data: deletedId } = yield call(axios.delete, `/dictionaries/${id}`);
    yield put(actions.fetchAllDictionaries());
}
