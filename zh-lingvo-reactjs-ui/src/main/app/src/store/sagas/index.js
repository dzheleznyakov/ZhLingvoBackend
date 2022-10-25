import { takeEvery, takeLatest } from 'redux-saga/effects';

import * as actionTypes from '../actionTypes';
import * as appSagas from './app';
import * as authSagas from './auth';
import * as dictionariesSagas from './dictionaries';
import * as quizzesSagas from './quizzes';
import * as quizRecordsSagas from './quizRecords';
import * as wordsSagas from './words';

export function* appWatcher() {
    yield takeEvery(actionTypes.SET_APP, appSagas.changeAppSaga);
    yield takeEvery(actionTypes.FETCH_ALL_LANGUAGES, appSagas.fetchAllLanguagesSaga);
    yield takeEvery(actionTypes.FETCH_POS, appSagas.fetchPosSaga);
}

export function* authWatcher() {    
    yield takeEvery(actionTypes.SIGN_IN, authSagas.logInSaga, '/signin');
    yield takeEvery(actionTypes.SIGN_UP, authSagas.logInSaga, '/signup');
    yield takeEvery(actionTypes.AUTO_SIGN_IN, authSagas.autoSignInSaga);
    yield takeEvery(actionTypes.SIGN_OUT, authSagas.signOutSaga);
}

export function* dictionariesWatcher() {
    yield takeEvery(actionTypes.FETCH_ALL_DICTIONARIES, dictionariesSagas.fetchAllDictionariesSaga);
    yield takeEvery(actionTypes.FETCH_DICTIONARY, dictionariesSagas.fetchDictionarySaga);

    yield takeEvery(actionTypes.CREATE_DICTIONARY, dictionariesSagas.createDictionarySaga);
    yield takeEvery(actionTypes.UPDATE_DICTIONARY, dictionariesSagas.updateDictionarySaga,);
    yield takeEvery(actionTypes.DELETE_DICTIONARY, dictionariesSagas.deleteDictionarySaga);
}

export function* quizzesWatcher() {
    yield takeEvery(actionTypes.FETCH_ALL_QUIZZES, quizzesSagas.fetchAllQuizzesSaga);

    yield takeEvery(actionTypes.CREATE_QUIZ, quizzesSagas.createQuizSaga);
    yield takeEvery(actionTypes.UPDATE_QUIZ, quizzesSagas.updateQuizSaga);
    yield takeEvery(actionTypes.DELETE_QUIZ, quizzesSagas.deleteQuizSaga);
    yield takeEvery(actionTypes.FETCH_QUIZ, quizzesSagas.fetchQuizSaga);

    yield takeLatest(actionTypes.FETCH_MATCHING_REGIMES, quizzesSagas.fetchMatchingRegimesSaga);
    yield takeLatest(actionTypes.FETCH_QUIZ_REGIMES, quizzesSagas.fetchQuizRegimesSaga);
    yield takeLatest(actionTypes.FETCH_QUIZ_SETTINGS, quizzesSagas.fetchQuizSettingsSaga);
    yield takeEvery(actionTypes.UPDATE_QUIZ_SETTINGS, quizzesSagas.updateQuizSettingsSaga);
}

export function* quizRecordsWatcher() {
    yield takeEvery(actionTypes.FETCH_QUIZ_RECORDS, quizRecordsSagas.fetchQuizRecords);
    yield takeEvery(actionTypes.CREATE_QUIZ_RECORD, quizRecordsSagas.createQuizSaga);
    yield takeEvery(actionTypes.DELETE_QUIZ_RECORD, quizRecordsSagas.deleteQuizRecordSaga);
    yield takeEvery(actionTypes.FETCH_QUIZ_RECORD, quizRecordsSagas.fetchQuizRecordSaga);
}

export function* wordsWatcher() {
    yield takeEvery(actionTypes.FETCH_WORDS_LIST, wordsSagas.fetchWordsListSaga);
    yield takeEvery(actionTypes.FETCH_WORD, wordsSagas.fetchWordSaga);
    yield takeEvery(actionTypes.CREATE_WORD, wordsSagas.createWordSaga);
    yield takeEvery(actionTypes.DELETE_SELECTED_WORD, wordsSagas.deleteSelectedWordSaga);
    yield takeEvery(actionTypes.UPDATE_WORD, wordsSagas.updateWordSaga);
}
