import { call, put, select } from 'redux-saga/effects';

import api from '../../axios-api';
import * as APPS from '../../static/constants/apps';
import * as paths from '../../static/constants/paths';
import * as actions from '../actions';
import * as selectors from '../selectors';

export function* changeAppSaga(action) {
    const { app } = action;

    let path = null;
    switch (app) {
        case APPS.TUTOR: 
            path = paths.TUTOR_ROOT; break;
        case APPS.DICTIONARY: 
        default: 
            path = paths.DICTIONARIES_ROOT;
    }

    path && (yield put(actions.navigateTo(path)));
}

export function* fetchAllLanguagesSaga() {
    const languages = yield select(selectors.languagesSelector);
    if (languages.length)
        return;

    try {
        const { data } = yield call(api.get, '/languages');
        yield put(actions.fetchAllLanguagesSuccess(data));
    } catch (error) {
        yield put(actions.addError(error.response.data, 'Erorr while fetching languages'));
    }
}
