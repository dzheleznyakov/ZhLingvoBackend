import { combineReducers } from 'redux';

import auth from './auth';
import app from './app';
import control from './control';
import dictionaries from './dictionaries';
import quizzes  from './quizzes';
import words from './words';

export default combineReducers({
    auth,
    app,
    control,
    dictionaries,
    quizzes,
    words,
});
