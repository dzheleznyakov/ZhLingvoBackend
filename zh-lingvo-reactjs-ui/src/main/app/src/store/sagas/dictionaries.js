import { call, put, select } from 'redux-saga/effects';

import axios from '../../axios-api';
import * as actions from '../actions';
import * as selectors from '../selectors';

export function* fetchAllDictionariesSaga() {
    yield put(actions.fetchAllDictionariesStart());

    try {
        const { data } = yield call(axios.get, '/dictionaries');
        yield put(actions.fetchAllDictionariesSuccess(data));
    } catch (error) {
        yield put(actions.addError(error.response.data, 'Erorr while fetching dictionaries'));
        yield put(actions.fetchAllDictionariesFailure());
    }
}

export function* fetchAllLanguagesSaga() {
    const languages = yield select(selectors.languagesSelector);
    if (languages.length)
        return;

    try {
        const { data } = yield call(axios.get, '/languages');
        yield put(actions.fetchAllLanguagesSuccess(data));
    } catch (error) {
        yield put(actions.addError(error.response.data, 'Erorr while fetching languages'));
    }
}

export function* createDictionarySaga(action) {
    const { name, language } = action;
    const newDictionary = { name, language}

    try {
        yield call(axios.post, '/dictionaries', newDictionary);
        yield put(actions.fetchAllDictionaries());
    } catch (error) {
        yield put(actions.addError(error.response.data, `Error while creating dictionary [${name}]`));
    }
}

export function* updateDictionarySaga(action) {
    const { name } = action;
    const currentDictionary = yield select(selectors.currentDictionarySelector);
    const updatedDictionary = { ...currentDictionary, name };

    try {
        yield call(axios.put, '/dictionaries', updatedDictionary);
        yield put(actions.fetchAllDictionaries());
    } catch (error) {
        yield put(actions.addError(error.response.data, `Error while updating dictionary [${name}]`));
    }
}

export function* deleteDictionarySaga(action) {
    const { id } = action;
    if (id < 0)
        return;

    try {
        yield call(axios.delete, `/dictionaries/${id}`);
        yield put(actions.fetchAllDictionaries());
    } catch (error) {
        yield put(actions.addError(error.response.data, `Error while deleteing dictionary [${id}]`));
    }
}
