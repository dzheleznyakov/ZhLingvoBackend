import { call, put } from 'redux-saga/effects';
import axios from '../../axios-api';

import * as actions from '../actions';
import { setCookie, getCookie, deleteCookie } from '../../utils/cookies';

const AUTH_TOKEN_COOKIE_FIELD = 'authToken';

export function* logInSaga(path, action) {
    const { username } = action;

    try {
        const response = yield call(axios.login, path, { username });
        yield put(actions.setUsername(username));
        
        const { token } = response.data;
        yield call(setCookie, AUTH_TOKEN_COOKIE_FIELD, token);
    } catch(err) {
        yield put(actions.setLoginError(err));
    }
}

export function* autoSignInSaga() {
    const token = yield call(getCookie, AUTH_TOKEN_COOKIE_FIELD);
    if (!token)
        return;

    try {
        const response = yield call(axios.login, '/resign', { token });

        const { username, token: authToken } = response.data;
        yield put(actions.setUsername(username));
        yield call(setCookie, AUTH_TOKEN_COOKIE_FIELD, authToken);
    } catch(err) {
        yield* signOutSaga();
    }
}

export function* signOutSaga() {
    yield put(actions.setUsername(null));
    yield call(deleteCookie, AUTH_TOKEN_COOKIE_FIELD);
}
