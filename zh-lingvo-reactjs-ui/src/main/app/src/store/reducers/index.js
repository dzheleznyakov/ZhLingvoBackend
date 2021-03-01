import { combineReducers } from 'redux';

import auth from './auth';
import app from './app';
import control from './control';

export default combineReducers({
    auth,
    app,
    control,
});
