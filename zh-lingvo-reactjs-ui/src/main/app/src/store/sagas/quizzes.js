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

export function* createQuizSaga(action) {
    const { name, targetLanguage } = action;
    const newQuiz = { name, targetLanguage };

    try {
        yield call(api.post, '/quizzes', newQuiz);
        yield put(actions.fetchAllQuizzes());
    } catch (error) {
        yield put(actions.addError(error.response.data, `Error while creating dictionary [${name}]`));
    }
}