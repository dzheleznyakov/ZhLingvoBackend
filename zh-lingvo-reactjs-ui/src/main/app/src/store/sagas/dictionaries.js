import { call, put, select } from 'redux-saga/effects';

import api from '../../axios-api';
import * as actions from '../actions';
import * as selectors from '../selectors';

export function* fetchAllDictionariesSaga() {
    yield put(actions.fetchAllDictionariesStart());

    try {
        const { data } = yield call(api.get, '/dictionaries');
        yield put(actions.fetchAllDictionariesSuccess(data));
    } catch (error) {
        yield put(actions.addError(error.response.data, 'Erorr while fetching dictionaries'));
        yield put(actions.fetchAllDictionariesFailure());
    }
}

export function* fetchDictionarySaga(action) {
    yield put(actions.fetchDictionaryStart());
    const { id } = action;
    const selectedDictionary = yield select(selectors.selectedDictionarySelector);
    if (selectedDictionary.id === id) {
        yield put(actions.fetchDictionarySuccess(selectedDictionary));
        return;
    }

    try {
        const { data } = yield call(api.get, `/dictionaries/${id}`);
        yield put(actions.fetchDictionarySuccess(data));
    } catch (error) {
        yield put(actions.addError(error.response.data, `Error while fetching dictionary [${id}]`));
        yield put(actions.fetchDictionaryFailure());
    }
}

export function* createDictionarySaga(action) {
    const { name, language } = action;
    const newDictionary = { name, language}

    try {
        yield call(api.post, '/dictionaries', newDictionary);
        yield put(actions.fetchAllDictionaries());
    } catch (error) {
        yield put(actions.addError(error.response.data, `Error while creating dictionary [${name}]`));
    }
}

export function* updateDictionarySaga(action) {
    const { name } = action;
    const currentDictionary = yield select(selectors.selectedDictionarySelector);
    const updatedDictionary = { ...currentDictionary, name };

    try {
        yield call(api.put, '/dictionaries', updatedDictionary);
        yield put(actions.fetchAllDictionaries());
    } catch (error) {
        yield put(actions.addError(error.response.data, `Error while updating dictionary [${currentDictionary.name}]`));
    }
}

export function* deleteDictionarySaga(action) {
    const { id } = action;
    if (id < 0)
        return;

    try {
        yield call(api.delete, `/dictionaries/${id}`);
        yield put(actions.fetchAllDictionaries());
    } catch (error) {
        yield put(actions.addError(error.response.data, `Error while deleteing dictionary [${id}]`));
    }
}
