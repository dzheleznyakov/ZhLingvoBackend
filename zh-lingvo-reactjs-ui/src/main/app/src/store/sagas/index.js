import { takeEvery } from 'redux-saga/effects';

import * as actionTypes from '../actionTypes/auth';
import * as authSagas from './auth';

export function* authWatcher() {    
    yield takeEvery(actionTypes.SIGN_IN, authSagas.logInSaga, '/signin');
    yield takeEvery(actionTypes.SIGN_UP, authSagas.logInSaga, '/signup');
}
