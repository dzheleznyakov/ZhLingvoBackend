import { call, put, all } from 'redux-saga/effects';
import axios from 'axios';

import { baseURL, headers } from '../../axios-api';
import * as actions from '../actions';

export function* logInSaga(path, action) {
    console.log(action, path);
    const { username } = action;

    try {
        const response = yield call(axios.post, `${baseURL}${path}`, { username }, { headers });
        yield put(actions.setUsername(username));
        
        const { token } = response.data;
        document.cookie = `authToken=${token}`;
    } catch(err) {
        yield put(actions.setLoginError(err));
    }
}
