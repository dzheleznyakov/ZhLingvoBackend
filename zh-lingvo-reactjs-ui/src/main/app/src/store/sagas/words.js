import { put, call } from 'redux-saga/effects';

import axios from '../../axios-api';
import * as actions from '../actions';

export function* fetchWordsListSaga(action) {
    const { dictionaryId } = action;
    try {
        const { data } = yield call(axios.get, `/words/dictionary/${dictionaryId}`);
        yield put(actions.fetchWordsListSuccess(data));
    } catch (error) {
        yield put(actions.addError(error.response.data, `Error while fetching words list for dictionary [${dictionaryId}]`));
    }
}
