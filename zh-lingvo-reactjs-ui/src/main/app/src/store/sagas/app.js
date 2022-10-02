import { put } from 'redux-saga/effects';

import * as APPS from '../../static/constants/apps';
import * as paths from '../../static/constants/paths';
import * as actions from '../actions';

export function* changeAppSaga(action) {
    const { app } = action;

    let path = null;
    switch (app) {
        case APPS.DICTIONARY: path = paths.DICTIONARIES_ROOT; break;
        case APPS.TUTOR: path = paths.TUTOR_ROOT; break;
    }

    path && (yield put(actions.navigateTo(path)));
}