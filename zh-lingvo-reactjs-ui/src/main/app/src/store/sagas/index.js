import { takeEvery } from 'redux-saga/effects';

import * as actionTypes from '../actionTypes/auth';
import * as authSagas from './auth';

export function* authWatcher() {    
    yield takeEvery(actionTypes.SIGN_IN, authSagas.logInSaga, '/signin');
    yield takeEvery(actionTypes.SIGN_UP, authSagas.logInSaga, '/signup');
    yield takeEvery(actionTypes.AUTO_SIGN_IN, authSagas.autoSignInSaga);
    yield takeEvery(actionTypes.CLEAR_USERNAME, authSagas.clearUsernameSaga);
}
