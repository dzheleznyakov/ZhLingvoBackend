import { all, call, put, select } from "redux-saga/effects";

import api from '../../axios-api';
import * as actions from '../actions';
import * as selectors from '../selectors';
import { quizSettingsSelector } from "../selectors";

export function* fetchAllQuizzesSaga() {
    yield put(actions.fetchAllQuizzesStart());

    try {
        const { data } = yield call(api.get, '/quizzes');
        yield put(actions.fetchAllQuizzesSuccess(data));
    } catch (error) {
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

export function* fetchQuizSaga(action) {
    yield put(actions.fetchQuizStart());
    const { id } = action;
    const selectedQuiz = yield select(selectors.selectedQuizSelector);
    if (selectedQuiz.id === id) {
        yield put(actions.fetchQuizSuccess(selectedQuiz));
        return;
    }

    try {
        const { data: fetchedQuiz } = yield call(api.get, `/quizzes/${id}`);
        yield put(actions.fetchQuizSuccess(fetchedQuiz))
    } catch (error) {
        yield all(
            put(actions.addError(error.response.data, `Error while fetching quiz [${id}]`)),
            put(actions.fetchQuizFailure()),
        );
    }
}

export function* fetchMatchingRegimesSaga() {
    const storedMatchingRegimes = yield select(selectors.matchingRegimesSelector);
    if (storedMatchingRegimes.length)
        return;

    try {
        const { data } = yield call(api.get, '/quizzes/matchingRegimes');
        yield put(actions.fetchMatchingRegimesSuccess(data));
    } catch (error) {
        yield put(actions.addError(error.response.data, 'Error while fetching quiz matching regimes'));
    }
}

export function* fetchQuizRegimesSaga() {
    const storedQuizRegimes = yield select(selectors.quizRegimeSelector);
    if (storedQuizRegimes.length)
        return;
    
    try {
        const { data } = yield call(api.get, '/quizzes/quizRegimes');
        yield put(actions.fetchQuizRegimesSuccess(data));
    } catch (error) {
        yield put(actions.addError(error.response.data, 'Error while fetching quiz regimes'));
    }
}

export function* fetchQuizSettingsSaga(action) {
    const { quizId } = action;
    const settings = (yield select(quizSettingsSelector))[quizId];

    if (settings) return;

    try {
        const { data } = yield call(api.get, `/quizzes/${quizId}/settings`);
        yield put(actions.fetchQuizSettingsSuccess(quizId, data));
    } catch (error) {
        yield put(actions.addError(error.response.data, `Error while fetching settings for quiz [${quizId}]`));
    }
}

export function* updateQuizSettingsSaga(action) {
    const { quizId, settings } = action;
        
    try {
        const { data } = yield call(api.put, `/quizzes/${quizId}/settings`, settings);
        yield put(actions.updateQuizSettingsSuccess(quizId, data));
    } catch (error) {
        yield put(actions.addError(error.response.data, `Error while updateing settings for quiz [${quizId}]`));
    }
}

export function* fetchAllQuizzesByLanguage(action) {
    const { language } = action;
    const { code: langCode } = language;

    yield put(actions.fetchAllQuizzesByLanguageStart());
    try {
        const { data: quizzes } = yield call(api.get, '/quizzes',  { params: { lang: langCode }});
        yield put(actions.fetchAllQuizzesByLanguageSuccess(quizzes));
    } catch (error) {
        yield put(actions.addError(error.response.data, 'Error while fetching quizzes'));
        yield put(actions.fetchAllQuizzesByLanguageFailure());
    }
}
