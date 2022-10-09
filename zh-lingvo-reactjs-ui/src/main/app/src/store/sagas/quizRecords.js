import { call, put } from "redux-saga/effects";

import api from '../../axios-api';
import * as actions from '../actions';

export function* fetchQuizRecords(action) {
    const { quizId } = action;

    try {
        const { data } = yield call(
            api.get, 
            `/quizzes/${quizId}/records/overviews`);
        yield put(actions.fetchQuizRecordsSuccess(data));
    } catch (error) {
        yield put(actions.addError(
            error.response.data,
            `Error while fetching quiz records for quiz [${quizId}]`));
    }
}
