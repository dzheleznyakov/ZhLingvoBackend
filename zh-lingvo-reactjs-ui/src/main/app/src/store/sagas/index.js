import { takeEvery } from 'redux-saga/effects';

import * as actionTypes from '../actionTypes';
import * as authSagas from './auth';
import * as dictionariesSagas from './dictionaries';

export function* authWatcher() {    
    yield takeEvery(actionTypes.SIGN_IN, authSagas.logInSaga, '/signin');
    yield takeEvery(actionTypes.SIGN_UP, authSagas.logInSaga, '/signup');
    yield takeEvery(actionTypes.AUTO_SIGN_IN, authSagas.autoSignInSaga);
    yield takeEvery(actionTypes.CLEAR_USERNAME, authSagas.clearUsernameSaga);
}

export function* dictionariesWatcher() {
    yield takeEvery(actionTypes.FETCH_ALL_DICTIONARIES, dictionariesSagas.fetchAllDictionariesSaga);
    yield takeEvery(actionTypes.FETCH_ALL_LANGUAGES, dictionariesSagas.fetchAllLanguagesSaga);
    yield takeEvery(actionTypes.CREATE_DICTIONARY, dictionariesSagas.createDictionarySaga);
}
