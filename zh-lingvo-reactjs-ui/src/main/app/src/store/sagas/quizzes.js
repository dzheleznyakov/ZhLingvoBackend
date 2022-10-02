import { call, put } from "redux-saga/effects";

import api from '../../axios-api';
import * as actions from '../actions';

export function* fetchAllQuizzesSaga() {
    yield put(actions.fetchAllQuizzesStart());

    try {
        const { data } = yield call(api.get, '/quizzes');
        yield put(actions.fetchAllQuizzesSuccess(data));
    } catch (error) {
        console.log(error);
        yield put(actions.addError(error.response.data, 'Error while fetching quizzes'));
        yield put(actions.fetchAllQuizzesFailure());
    }
}