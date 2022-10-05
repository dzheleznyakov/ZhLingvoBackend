import { call, put, select } from "redux-saga/effects";

import api from '../../axios-api';
import * as actions from '../actions';
import * as selectors from '../selectors';

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

export function* updateQuizSaga(action) {
    const { name } = action;
    const currentQuiz = yield select(selectors.selectedQuizSelector);
    const updatedQuiz = { ...currentQuiz, name };

    try {
        yield call(api.put, '/quizzes', updatedQuiz);
        yield put(actions.fetchAllQuizzes());
    } catch (error) {
        yield put(actions.addError(error.response.data, `Error while updating quiz [${currentQuiz.name}]`))
    }
}

export function* deleteQuizSaga(action) {
    const { id } = action;
    if (id < 0)
        return;

    try {
        yield call(api.delete, `/quizzes/${id}`)
        yield put(actions.fetchAllQuizzes());
    } catch (error) {
        yield put(actions.addError(error.response.data, `Error while deleting quiz [${id}]`));
    }
}