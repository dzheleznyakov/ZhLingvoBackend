import { takeEvery } from 'redux-saga/effects';

import * as actionTypes from '../actionTypes';
import * as authSagas from './auth';
import * as dictionariesSagas from './dictionaries';
import * as wordsSagas from './words';

export function* authWatcher() {    
    yield takeEvery(actionTypes.SIGN_IN, authSagas.logInSaga, '/signin');
    yield takeEvery(actionTypes.SIGN_UP, authSagas.logInSaga, '/signup');
    yield takeEvery(actionTypes.AUTO_SIGN_IN, authSagas.autoSignInSaga);
    yield takeEvery(actionTypes.SIGN_OUT, authSagas.signOutSaga);
}

export function* dictionariesWatcher() {
    yield takeEvery(actionTypes.FETCH_ALL_DICTIONARIES, dictionariesSagas.fetchAllDictionariesSaga);
    yield takeEvery(actionTypes.FETCH_DICTIONARY, dictionariesSagas.fetchDictionarySaga);
    yield takeEvery(actionTypes.FETCH_ALL_LANGUAGES, dictionariesSagas.fetchAllLanguagesSaga);

    yield takeEvery(actionTypes.CREATE_DICTIONARY, dictionariesSagas.createDictionarySaga);
    yield takeEvery(actionTypes.UPDATE_DICTIONARY, dictionariesSagas.updateDictionarySaga,);
    yield takeEvery(actionTypes.DELETE_DICTIONARY, dictionariesSagas.deleteDictionarySaga);
}

export function* wordsWatcher() {
    yield takeEvery(actionTypes.FETCH_WORDS_LIST, wordsSagas.fetchWordsListSaga);
    yield takeEvery(actionTypes.FETCH_WORD, wordsSagas.fetchWordSaga);
}
